package net.enelson.sopclans.data.clan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.database.SQLClanParameter;
import net.enelson.sopclans.utils.Utils;

public class Clan {
	private int id;
	private List<Member> members;
	private String name;
	private String tag;
	private int level;
	private double exp;
	private int slotCount;
	private boolean isPvpEnabled;
	private Location clanhome;
	
	public Clan(int id, Player leader, String name, int slots) {
		this.id = id;
		this.name = name;
		this.tag = "";
		this.level = 1;
		this.members = new ArrayList<Member>();
		Member member = new Member(leader, id, SopClans.rm.getRank("leader"), 0.0);
		this.members.add(member);
		this.slotCount = slots;
		this.isPvpEnabled = false;
		this.clanhome = null;
		
		SopClans.sql.addMember(id,member);	
	}
	
	public Clan(int id, String name, String tag, int level, double exp, int slots, boolean isPvpEnabled, String clanhome) {
		this.id = id;
		this.name = name;
		this.tag = tag != null ? tag : "";
		this.level = level;
		this.exp = exp;
		this.slotCount = slots;
		this.isPvpEnabled = isPvpEnabled;
		
		this.members = SopClans.sql.getMembers(id);
		if(this.members.stream().filter(m -> m.getRank().getName().equals("leader")).count() != 1) {
			Bukkit.getLogger().warning("Р СџРЎР‚Р С‘ Р В·Р В°Р С–РЎР‚РЎС“Р В·Р С”Р Вµ Р Т‘Р В°Р Р…Р Р…РЎвЂ№РЎвЂ¦ Р С”Р В»Р В°Р Р…Р В° ("+id+") Р С—РЎР‚Р С•Р С‘Р В·Р С•РЎв‚¬Р В»Р В° Р С•РЎв‚¬Р С‘Р В±Р С”Р В°. Р С™Р С•Р В»Р С‘РЎвЂЎР ВµРЎРѓРЎвЂљР Р†Р С• Р В»Р С‘Р Т‘Р ВµРЎР‚Р С•Р Р† Р Р† Р вЂР вЂќ != 1.");
			SopClans.plugin.getPluginLoader().disablePlugin(SopClans.plugin);
			return;
		}
		if(clanhome != null && !clanhome.equals(""))
			this.clanhome = Utils.getDeserializedLocation(clanhome);
		else
			this.clanhome = null;
	}
	
	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public double getExp() {
		return this.exp;
	}
	
	public int getSlotCount() {
		return this.slotCount;
	}
	
	public int getMembersCount() {
		return this.members.size();
	}
	
	public List<Member> getMembers() {
		return this.members;
	}

	public void setExp(Double exp) {
		this.exp = exp;
		SopClans.sql.updateClanParameter(this.id, SQLClanParameter.EXP, this.exp);
	}
	
	public void addLevel() {
		this.level++;
		SopClans.sql.updateClanParameter(this.id, SQLClanParameter.EXP, this.level);
	}

	public void addExp(Double exp) {
		this.exp += exp;
		SopClans.sql.updateClanParameter(this.id, SQLClanParameter.EXP, this.exp);
	}
	
	public int addSlotCount(int slotCount) {
		this.slotCount += slotCount;
		SopClans.sql.updateClanParameter(this.id, SQLClanParameter.SLOTS, this.slotCount);
		return this.slotCount;
	}

	public void setName(String name) {
		this.name = name;
		SopClans.sql.updateClanParameter(this.id, SQLClanParameter.NAME, name);
	}
	
	public void setTag(String tag) {
		this.tag = tag;
		SopClans.sql.updateClanParameter(this.id, SQLClanParameter.TAG, tag);
	}
	
	public void addMember(Player player) {
		Member member = new Member(player, this.id, SopClans.rm.getDefaultRank(), 0.0);
		this.members.add(member);
		SopClans.sql.addMember(this.id, member);
	}
	
	public boolean removeMember(Member member) {
		boolean b = this.members.contains(member) && !member.getRank().isLeader();
		if(b) {
			this.members.remove(member);
			SopClans.sql.removeMember(member);
		}
		return b;
	}
	
	public Member getLeader() {
		return this.members.stream().filter(m -> m.getRank().getName().equals("leader")).findFirst().orElse(null);
	}
	
	public Member getMember(Player player) {
		return this.members.stream().filter(m -> m.getUniqueId().equals(player.getUniqueId().toString())).findFirst().orElse(null);
	}
	
	public Member getMember(String playerName) {
		return this.members.stream().filter(m -> m.getPlayerName().equalsIgnoreCase(playerName)).findFirst().orElse(null);
	}
	
	public void transfer(Member newLeader) {
		Member oldLeader = this.getLeader();
		oldLeader.setRank(newLeader.getRank());
		newLeader.setRank(SopClans.rm.getRank("leader"));
	}
	
	public void broadcastMembers(String message) {
		this.members.forEach(m -> {
			Player p = Bukkit.getPlayer(UUID.fromString(m.getUniqueId()));
			if(p != null)
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, message)));
		});
	}

	public void switchPvp() {
		this.isPvpEnabled = !this.isPvpEnabled;
		SopClans.sql.updateClanParameter(this.id, SQLClanParameter.ISPVPENABLED, this.isPvpEnabled);
	}
	
	public boolean isPvpEnabled() {
		return this.isPvpEnabled;
	}

	public Location getClanhome() {
		return this.clanhome;
	}
	
	public void setClanhome(Location clanhome) {
		this.clanhome = clanhome;
		SopClans.sql.updateClanParameter(this.id, SQLClanParameter.CLANHOME, Utils.getSerializedLocation(this.clanhome));
	}
	
	public void remove() {
		SopClans.sql.removeMembers(this.id);
	}
}

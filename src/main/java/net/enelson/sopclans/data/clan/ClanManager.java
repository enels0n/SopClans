package net.enelson.sopclans.data.clan;

import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.database.SQLClanParameter;
import net.enelson.sopclans.utils.Message;

public class ClanManager {

	private List<Clan> clans;

	public ClanManager() {
		this.clans = SopClans.sql.getClans();
	}

	public Clan getClan(Player player) {
		for (Clan clan : clans) {
			if (clan.getMember(player) != null)
				return clan;
		}
		return null;
	}

	public Clan getClanByName(String clanName) {
		for (Clan clan : clans) {
			if (clan.getName().equalsIgnoreCase(clanName))
				return clan;
		}
		return null;
	}

	public Clan getClanByTag(String clanTag) {
		for (Clan clan : clans) {
			if (clan.getTag().equalsIgnoreCase(clanTag))
				return clan;
		}
		return null;
	}

	public Clan getClan(int clanId) {
		for (Clan clan : clans) {
			if (clan.getId() == clanId)
				return clan;
		}
		return null;
	}

	public Clan createClan(Player leader, String clanName) {
		int minSlots = SopClans.configMain.getInt("clan.min_slots");
		int id = SopClans.sql.addClan(clanName, minSlots);
		if (id == 0)
			return null;

		Clan clan = new Clan(id, leader, clanName, minSlots);
		this.clans.add(clan);
		return clan;
	}

	public List<Clan> getClans() {
		return this.clans;
	}

	public void removeClan(Clan clan) {
		SopClans.sql.updateClanParameter(clan.getId(), SQLClanParameter.ISACTIVE, false);
		clan.remove();
		this.clans.remove(clan);
	}
	
	
	public Clan getTopClan(int position) {
		this.topSort();
		return (this.clans.size() < position) ? null : this.clans.get(position - 1);
	}
	
	private void topSort() {
		Collections.sort(this.clans, (o1, o2) -> {

			Integer x11 = ((Clan) o1).getLevel();
			Integer x21 = ((Clan) o2).getLevel();
			int sComp = x21.compareTo(x11);

			if (sComp != 0) {
				return sComp;
			}

			Double x12 = ((Clan) o1).getExp();
			Double x22 = ((Clan) o2).getExp();
			return x22.compareTo(x12);
		});
	}

	public void addExp(Clan clan, Player player, double exp) {
		Member member = clan.getMember(player);
		member.addExp(exp);
		clan.addExp(exp);
		Double nextLevel = SopClans.configMain.getDouble("clan.levels."+(clan.getLevel()+1));
		if(nextLevel == 0)
			return;
		
		if(clan.getExp()>nextLevel) {
			clan.setExp(clan.getExp()-nextLevel);
			clan.addLevel();
			clan.broadcastMembers(Message.CLAN_LEVELUP_BROADCAST.getMessage()
					.replaceAll("%level%", clan.getLevel()+""));
		}
	}
}

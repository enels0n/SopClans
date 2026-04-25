package net.enelson.sopclans.data.member;

import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.rank.Rank;
import net.enelson.sopclans.database.SQLMemberParameter;

public class Member {
	private String uuid;
	private String playerName;
	private int clanId;
	private Rank rank;
	private Double exp;
	
	public Member(Player player, int clanId, Rank rank, Double exp) {
		this.uuid = player.getUniqueId().toString();
		this.playerName = player.getName();
		this.clanId = clanId;
		this.rank = rank;
		this.exp = exp;
	}
	
	public Member(String uuid, String playerName, int clanId, Rank rank, Double exp) {
		this.uuid = uuid;
		this.playerName = playerName;
		this.clanId = clanId;
		this.rank = rank;
		this.exp = exp;
	}
	
	public String getUniqueId() {
		return this.uuid;
	}
	
	public String getPlayerName() {
		return this.playerName;
	}
	
	public int getClanId() {
		return this.clanId;
	}
	
	public Rank getRank() {
		return this.rank;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
		SopClans.sql.updateMemberParameter(this.clanId, this.uuid, SQLMemberParameter.RANK, this.rank.getName());
	}
	
	public boolean demote() {
		Rank newRank = SopClans.rm.getPreviousRank(this.rank);
		if(newRank == null)
			return false;
		this.rank = newRank;
		return true;
	}
	
	public Double getExp() {
		return this.exp;
	}
	
	public void addExp(Double exp) {
		this.exp += exp;
		SopClans.sql.updateMemberParameter(this.clanId, this.uuid, SQLMemberParameter.EXP, this.exp);
	}
}

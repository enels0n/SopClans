package net.enelson.sopclans.data.clanwars;

public class ClanWar {

	private int attackerClanId;
	private int defenderClanId;
	private Long startTime;
	private double attackerMultiplier;
	private double defenderMultiplier;
	
	public ClanWar(int attackerClanId, int defenderClanId, Long startTime, double attackerMultiplier, double defenderMultiplier) {
		this.attackerClanId = attackerClanId;
		this.defenderClanId = defenderClanId;
		this.startTime = startTime;
		this.attackerMultiplier = attackerMultiplier;
		this.defenderMultiplier = defenderMultiplier;
	}
	
	public int getAttackerClanId() {
		return this.attackerClanId;
	}
	
	public int getDefenderClanId() {
		return this.defenderClanId;
	}
	
	public Long getStartTime() {
		return this.startTime;
	}
	
	public double getAttackerMultiplier() {
		return this.attackerMultiplier;
	}
	
	public double getDefenderMultiplier() {
		return this.defenderMultiplier;
	}
}

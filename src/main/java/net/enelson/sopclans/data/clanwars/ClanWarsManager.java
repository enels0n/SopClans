package net.enelson.sopclans.data.clanwars;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;

public class ClanWarsManager {

	private List<ClanWar> wars;
	private Double attakerMultiplier;
	private Double defenderMultiplier;
	private BukkitTask runTaskSurvivalParameter;
	private Long warDuration;

	public ClanWarsManager(Plugin plugin) {
		wars = new ArrayList<ClanWar>();
		this.attakerMultiplier = SopClans.configMain.getDouble("clan.war_multiplier.attacker");
		this.defenderMultiplier = SopClans.configMain.getDouble("clan.war_multiplier.defender");
		this.warDuration = SopClans.configMain.getLong("clan.war_duration");

        loadClanWars();
		
		this.runTaskSurvivalParameter = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin,
				new Runnable() {
					@Override
					public void run() {
						Iterator<ClanWar> it = wars.iterator();
						while (it.hasNext()) {
							ClanWar cw = it.next();
							Clan attacker = SopClans.cm.getClan(cw.getAttackerClanId());
							Clan defender = SopClans.cm.getClan(cw.getDefenderClanId());

							if (cw.getStartTime()+warDuration < System
									.currentTimeMillis() / 1000) {
								String message = "Р вЂ™Р С•Р в„–Р Р…Р В° Р Р†Р В°РЎв‚¬Р ВµР С–Р С• Р С”Р В»Р В°Р Р…Р В° Р С—РЎР‚Р С•РЎвЂљР С‘Р Р† Р С”Р В»Р В°Р Р…Р В° %clan% Р С•Р С”Р С•Р Р…РЎвЂЎР ВµР Р…Р В°!";
								attacker.broadcastMembers(ChatColor.translateAlternateColorCodes('&',
										message.replace("%clan%", defender.getName())));
								defender.broadcastMembers(ChatColor.translateAlternateColorCodes('&',
										message.replace("%clan%", attacker.getName())));
								
								SopClans.sql.setClanWar(cw.getAttackerClanId(), cw.getDefenderClanId(), cw.getStartTime(),
												cw.getAttackerMultiplier(), cw.getDefenderMultiplier(), false);
								it.remove();
							}
						}
					}
				}, 10 * 20, 10 * 20);
	}

    public ClanWar createClanWar(Clan attacker, Clan defender) {
        long startTime = System.currentTimeMillis() / 1000;
        ClanWar cw = new ClanWar(attacker.getId(), defender.getId(), startTime,
                this.attakerMultiplier, this.defenderMultiplier);
        this.wars.add(cw);
        SopClans.sql.setClanWar(cw.getAttackerClanId(), cw.getDefenderClanId(), cw.getStartTime(),
                cw.getAttackerMultiplier(), cw.getDefenderMultiplier(), true);
        return cw;
    }
    
	public Double getMultiplier(Clan attacker, Clan defender) {
		for (ClanWar cw : this.wars) {
			if ((cw.getAttackerClanId() == attacker.getId() && cw.getDefenderClanId() == defender.getId()))
				return cw.getAttackerMultiplier();
			if ((cw.getAttackerClanId() == defender.getId() && cw.getDefenderClanId() == attacker.getId()))
				return cw.getDefenderMultiplier();
		}

		return 1.0;
	}

	public boolean isWarred(Clan attacker, Clan defender) {
		for (ClanWar cw : this.wars) {
			if ((cw.getAttackerClanId() == attacker.getId() && cw.getDefenderClanId() == defender.getId())
					|| (cw.getAttackerClanId() == defender.getId() && cw.getAttackerClanId() == attacker.getId()))
				return true;
		}
		return false;
	}
	
    private void loadClanWars() {
        List<ClanWar> existingWars = SopClans.sql.getClanWars();
        wars.addAll(existingWars);
    }

	public void deInit() {
		this.runTaskSurvivalParameter.cancel();
	}
}

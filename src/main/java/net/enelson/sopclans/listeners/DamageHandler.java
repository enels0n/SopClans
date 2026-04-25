package net.enelson.sopclans.listeners;

import java.util.Random;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.utils.ExpSystem;
import net.enelson.sopclans.utils.Message;

public class DamageHandler implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.isCancelled())
			return;

		if (!(e.getEntity() instanceof Player))
			return;

		Player player = (Player) e.getEntity();
		
		Clan defenderClan = SopClans.cm.getClan(player);
		if (defenderClan == null)
			return;

		switch (e.getCause()) {
		case PROJECTILE:
		case MAGIC:
		case ENTITY_ATTACK:
		case ENTITY_SWEEP_ATTACK:
		case ENTITY_EXPLOSION:
		case WITHER:
		case THORNS:
			ProjectileSource source = null;

			if (e.getDamager() instanceof Projectile) {
				Projectile projectile = (Projectile) e.getDamager();
				source = projectile.getShooter();
			} else if (e.getDamager() instanceof AreaEffectCloud) {
				AreaEffectCloud a = (AreaEffectCloud) e.getDamager();
				source = a.getSource();
			} else if (e.getDamager() instanceof Player) {
				source = (ProjectileSource) e.getDamager();
			}

			if (!(source != null && source instanceof Player))
				return;
			
			Player damager = (Player) source;
			
			Clan attackerClan = SopClans.cm.getClan(damager);
			if (attackerClan == null)
				return;
			
			if(attackerClan == defenderClan) {
				if(!attackerClan.isPvpEnabled()) {
					e.setCancelled(true);
					damager.sendMessage(Message.CANNOT_ATTACK_CLANMATE.getMessageWithPlaceholders(player));
				}
				return;
			}

			ExpSystem system = ExpSystem.valueOf(SopClans.configMain.getString("clan.exp.system").toUpperCase());
			Double damage = e.getFinalDamage();
			Double multiplier = SopClans.cwm.getMultiplier(attackerClan, defenderClan);
			Double exp = 0.0;
			Random rnd = new Random();
			
			if((system.equals(ExpSystem.KILL) || system.equals(ExpSystem.COMBINED)) && ((LivingEntity)player).getHealth()-damage<=0) {
				Double min = SopClans.configMain.getDouble("clan.exp.for_kill.min");
				Double max = SopClans.configMain.getDouble("clan.exp.for_kill.max");
				exp += Math.ceil((min + (max - min) * rnd.nextDouble())*100) / 100;
			}
			if(system.equals(ExpSystem.DAMAGE) || system.equals(ExpSystem.COMBINED)) {
				damage = (((LivingEntity)player).getHealth()-damage<=0) ? ((LivingEntity)player).getHealth() : damage;
				Double min = SopClans.configMain.getDouble("clan.exp.for_damage.min");
				Double max = SopClans.configMain.getDouble("clan.exp.for_damage.max");
				exp += Math.ceil((damage*(min + (max - min) * rnd.nextDouble())*100)) / 100;
				
			}
			SopClans.cm.addExp(attackerClan, damager, exp*multiplier);
		default:
			return;
		}
	}
}

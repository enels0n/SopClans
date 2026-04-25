package net.enelson.sopclans.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class WarCommand {

	public WarCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}

		Player inviter = (Player)sender;
		
		if(args.length != 1) {
			sender.sendMessage(Message.INVALID_ARGUMENTS.getMessageWithPlaceholders(inviter));
			return;
		}
		
		Clan attacker = SopClans.cm.getClan(inviter);
		if(attacker == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(inviter));
			return;
		}
		
		Member member = attacker.getMember(inviter);
		if(!member.getRank().getPermission("can_declare_war")) {
			inviter.sendMessage(Message.NO_ACCESS_WAR.getMessageWithPlaceholders(inviter));
			return;
		}
		
		Clan defender = SopClans.cm.getClan(Integer.parseInt(args[0]));
		if(defender == null) {
			inviter.sendMessage(Message.CLAN_NOT_FOUND.getMessageWithPlaceholders(inviter));
			return;
		}
		
		if(attacker == defender) {
			inviter.sendMessage(Message.CANNOT_DECLARE_YOUSELF.getMessageWithPlaceholders(inviter));
			return;
		}
		
		if(SopClans.cwm.isWarred(attacker, defender)) {
			inviter.sendMessage(Message.CLAN_IS_ALREADY_WAR.getMessageWithPlaceholders(inviter));
			return;
		}

		Double price = SopClans.configMain.getDouble("economy.declare_war_price");
		if(SopClans.economy != null) {
			if(SopClans.economy.getBalance(inviter) < price) {
				inviter.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(inviter).replaceAll("%price%", price+""));
				return;
			}

			inviter.sendMessage(Message.CHARGED_DEMOTE.getMessageWithPlaceholders(inviter).replaceAll("%price%", price+""));
			SopClans.economy.withdrawPlayer(inviter, price);
		}

		SopClans.cwm.createClanWar(attacker, defender);
		
		String messageAttacker = Message.DECLARE_WAR_ATTACKER_1.getMessageWithPlaceholders(inviter)
				.replaceAll("%rank%", member.getRank().getDisplayName())
				.replaceAll("%player%", inviter.getName());
		
		messageAttacker += " " + Message.DECLARE_WAR_ATTACKER_2.getMessage();

		attacker.broadcastMembers(messageAttacker.replace("%defender%", defender.getName()));
		defender.broadcastMembers(Message.DECLARE_WAR_DEFENDER.getMessage().replace("%attacker%", attacker.getName()));
	}
}

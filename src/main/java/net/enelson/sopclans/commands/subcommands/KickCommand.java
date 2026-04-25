package net.enelson.sopclans.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class KickCommand {
	/* 
	 * Usage: /clan kick playerName
	 */
	public KickCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}

		Player kicker = (Player)sender;
		
		if(args.length != 1) {
			sender.sendMessage(Message.INVALID_ARGUMENTS.getMessageWithPlaceholders(kicker));
			return;
		}
		
		Clan clan = SopClans.cm.getClan(kicker);
		if(clan == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(kicker));
			return;
		}
		
		Member member = clan.getMember(kicker);
		if(!member.getRank().getPermission("can_kick")) {
			kicker.sendMessage(Message.NO_ACCESS_KICK.getMessageWithPlaceholders(kicker));
			return;
		}
		
		Member kickedMember = clan.getMember(args[0]);
		if(kickedMember == null) {
			kicker.sendMessage(Message.KICK_MEMBER_NOT_FOUND.getMessageWithPlaceholders(kicker));
			return;
		}
		
		if(SopClans.rm.getRankPriority(member.getRank()) >= SopClans.rm.getRankPriority(kickedMember.getRank())) {
			kicker.sendMessage(Message.KICK_LOWER_RANK.getMessageWithPlaceholders(kicker));
			return;
		}
		
		double kickPrice = SopClans.configMain.getDouble("economy.kick_price");
		if(SopClans.economy != null && kickPrice>0) {
			if(SopClans.economy.getBalance(kicker) < kickPrice) {
				kicker.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(kicker).replaceAll("%price%", kickPrice+""));
				return;
			}
			
			kicker.sendMessage(Message.CHARGED_DEMOTE.getMessageWithPlaceholders(kicker).replaceAll("%price%", kickPrice+""));
			SopClans.economy.withdrawPlayer(kicker, kickPrice);
		}
		
		if(!clan.removeMember(kickedMember)) {
			kicker.sendMessage(Message.KICK_ERROR.getMessageWithPlaceholders(kicker));
			return;
		}
		
		String message = Message.KICK_1.getMessage()
				.replaceAll("%rank%", member.getRank().getDisplayName())
				.replaceAll("%player%", kicker.getName());
		
		message += " " + Message.KICK_2.getMessage();
		
		Player kickedPlayer = Bukkit.getPlayerExact(kickedMember.getPlayerName());
		if(kickedPlayer != null) {
			message += " " + Message.KICK_3.getMessageWithPlaceholders(kickedPlayer)
					.replaceAll("%rank%", kickedMember.getRank().getDisplayName())
					.replaceAll("%player%", kickedMember.getPlayerName());
		}
		else {
			message += " " + Message.KICK_3.getMessage()
					.replaceAll("%rank%", kickedMember.getRank().getDisplayName())
					.replaceAll("%player%", kickedMember.getPlayerName());
		}
		
		message += " " + Message.KICK_4.getMessage();
		
		clan.broadcastMembers(message);
	}
}

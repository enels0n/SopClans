package net.enelson.sopclans.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.data.rank.Rank;
import net.enelson.sopclans.utils.Message;

public class PromoteCommand {
	/* 
	 * Usage: /clan promote playerName
	 */
	public PromoteCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}

		Player promoterPlayer = (Player)sender;
		
		if(args.length != 1) {
			sender.sendMessage(Message.INVALID_ARGUMENTS.getMessageWithPlaceholders(promoterPlayer));
			return;
		}
		
		Clan clan = SopClans.cm.getClan(promoterPlayer);
		if(clan == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(promoterPlayer));
			return;
		}
		
		Member promoterMember = clan.getMember(promoterPlayer);
		if(!promoterMember.getRank().getPermission("can_promote")) {
			promoterPlayer.sendMessage(Message.NO_ACCESS_PROMOTE.getMessageWithPlaceholders(promoterPlayer));
			return;
		}
		
		Member promotedMember = clan.getMember(args[0]);
		if(promotedMember == null) {
			promoterPlayer.sendMessage(Message.NO_MEMBER_FOUND.getMessageWithPlaceholders(promoterPlayer));
			return;
		}
		
		Rank nextRank = SopClans.rm.getNextRank(promotedMember.getRank());
		if(SopClans.rm.getRankPriority(promoterMember.getRank()) >= SopClans.rm.getRankPriority(nextRank)) {
			promoterPlayer.sendMessage("Р вЂ™РЎвЂ№ Р СР С•Р В¶Р ВµРЎвЂљР Вµ Р С—Р С•Р Р†РЎвЂ№РЎв‚¬Р В°РЎвЂљРЎРЉ Р С‘Р С–РЎР‚Р С•Р С”Р С•Р Р† РЎвЂљР С•Р В»РЎРЉР С”Р С• Р Т‘Р С• Р В·Р Р†Р В°Р Р…Р С‘РЎРЏ Р Р…Р С‘Р В¶Р Вµ Р Р†Р В°РЎв‚¬Р ВµР С–Р С•.");
			return;
		}
		
		if(SopClans.economy != null && nextRank.getPromotePrice()>0) {
			if(SopClans.economy.getBalance(promoterPlayer) < nextRank.getPromotePrice()) {
				promoterPlayer.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(promoterPlayer).replaceAll("%price%", nextRank.getPromotePrice()+""));
				return;
			}
			
			promoterPlayer.sendMessage(Message.CHARGED_PROMOTE.getMessageWithPlaceholders(promoterPlayer).replaceAll("%price%", nextRank.getPromotePrice()+""));
			SopClans.economy.withdrawPlayer(promoterPlayer, nextRank.getPromotePrice());
		}
		
		String message = Message.PROMOTE_PLAYER_1.getMessageWithPlaceholders(promoterPlayer)
				.replaceAll("%rank%", promoterMember.getRank().getDisplayName())
				.replaceAll("%player%", promoterPlayer.getName());
		
		message += " " + Message.PROMOTE_PLAYER_2.getMessage();
		
		Player promotedPlayer = Bukkit.getPlayerExact(promotedMember.getPlayerName());
		if(promotedPlayer != null) {
			message += " " + Message.PROMOTE_PLAYER_3.getMessageWithPlaceholders(promotedPlayer)
					.replaceAll("%rank%", promotedMember.getRank().getDisplayName())
					.replaceAll("%player%", promotedMember.getPlayerName());
		}
		else {
			message += " " + Message.PROMOTE_PLAYER_3.getMessage()
					.replaceAll("%rank%", promotedMember.getRank().getDisplayName())
					.replaceAll("%player%", promotedMember.getPlayerName());
		}
		message += " " + Message.PROMOTE_PLAYER_4.getMessage().replaceAll("%rank%", nextRank.getDisplayName());
		
		clan.broadcastMembers(message);
		
		promotedMember.setRank(nextRank);
	}
}

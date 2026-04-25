package net.enelson.sopclans.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.data.rank.Rank;
import net.enelson.sopclans.utils.Message;

public class DemoteCommand {
	/* 
	 * Usage: /clan demote playerName
	 */
	public DemoteCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}

		Player demoterPlayer = (Player)sender;
		
		if(args.length != 1) {
			sender.sendMessage(Message.INVALID_ARGUMENTS.getMessageWithPlaceholders(demoterPlayer));
			return;
		}
		
		Clan clan = SopClans.cm.getClan(demoterPlayer);
		if(clan == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(demoterPlayer));
			return;
		}
		
		Member demoterMember = clan.getMember(demoterPlayer);
		if(!demoterMember.getRank().getPermission("can_demote")) {
			demoterPlayer.sendMessage(Message.NO_ACCESS_DEMOTE.getMessageWithPlaceholders(demoterPlayer));
			return;
		}
		
		Member demotedMember = clan.getMember(args[0]);
		if(demotedMember == null) {
			demoterPlayer.sendMessage(Message.NO_MEMBER_FOUND.getMessageWithPlaceholders(demoterPlayer));
			return;
		}
		
		if(SopClans.rm.getRankPriority(demoterMember.getRank()) >= SopClans.rm.getRankPriority(demotedMember.getRank())) {
			demoterPlayer.sendMessage(Message.DEMOTE_LOWER_RANK.getMessageWithPlaceholders(demoterPlayer));
			return;
		}

		Rank previousRank = SopClans.rm.getPreviousRank(demotedMember.getRank());
		if(previousRank == null) {
			demoterPlayer.sendMessage(Message.PLAYER_CANNOT_DEMOTED.getMessageWithPlaceholders(demoterPlayer));
			return;
		}

		if(SopClans.economy != null && demotedMember.getRank().getDemotePrice()>0) {
			if(SopClans.economy.getBalance(demoterPlayer) < demotedMember.getRank().getDemotePrice()) {
				demoterPlayer.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(demoterPlayer).replaceAll("%price%", demotedMember.getRank().getDemotePrice()+""));
				return;
			}
			
			demoterPlayer.sendMessage(Message.CHARGED_DEMOTE.getMessageWithPlaceholders(demoterPlayer).replaceAll("%price%", demotedMember.getRank().getDemotePrice()+""));
			SopClans.economy.withdrawPlayer(demoterPlayer, demotedMember.getRank().getDemotePrice());
		}
		
		String message = Message.DEMOTE_PLAYER_1.getMessageWithPlaceholders(demoterPlayer)
				.replaceAll("%rank%", demoterMember.getRank().getDisplayName())
				.replaceAll("%player%", demoterPlayer.getName());
		
		message += " " + Message.DEMOTE_PLAYER_2.getMessage();
		
		Player demotedPlayer = Bukkit.getPlayerExact(demotedMember.getPlayerName());
		if(demotedPlayer != null) {
			message += " " + Message.DEMOTE_PLAYER_3.getMessageWithPlaceholders(demotedPlayer)
					.replaceAll("%rank%", demotedMember.getRank().getDisplayName())
					.replaceAll("%player%", demotedMember.getPlayerName());
		}
		else {
			message += " " + Message.DEMOTE_PLAYER_3.getMessage()
					.replaceAll("%rank%", demotedMember.getRank().getDisplayName())
					.replaceAll("%player%", demotedMember.getPlayerName());
		}
		message += " " + Message.DEMOTE_PLAYER_4.getMessage().replaceAll("%rank%", previousRank.getDisplayName());
				
		clan.broadcastMembers(message);
		
		demotedMember.setRank(previousRank);
	}
}

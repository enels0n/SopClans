package net.enelson.sopclans.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class TransferCommand {
	/* 
	 * Usage: /clan transfer
	 */
	public TransferCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}
		
		Player player = (Player)sender;
		Clan clan = SopClans.cm.getClan(player);
		if(clan == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(player));
			return;
		}
		
		if(!clan.getLeader().getUniqueId().equals(player.getUniqueId().toString())) {
			player.sendMessage(Message.IS_NOT_LEADER.getMessageWithPlaceholders(player));
			return;
		}
		
		Member newLeader = clan.getMember(args[0]);
		if(newLeader == null) {
			player.sendMessage(Message.NO_MEMBER_FOUND.getMessageWithPlaceholders(player));
			return;
		}
		
		clan.transfer(newLeader);
		String message = Message.TRANSFER_CLAN_1.getMessage();
		
		Player playerNewLeader = Bukkit.getPlayerExact(newLeader.getPlayerName());
		if(playerNewLeader != null)
			message += " " + Message.TRANSFER_CLAN_2.getMessageWithPlaceholders(playerNewLeader);
		else
			message += " " + Message.TRANSFER_CLAN_2.getMessage();
		
		clan.broadcastMembers(message);
		
		player.sendMessage(Message.TRANSFER_CLAN_OLD_LEADER.getMessageWithPlaceholders(player).replaceAll("%rank%", clan.getMember(player).getRank().getDisplayName()));
	}
}

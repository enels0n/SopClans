package net.enelson.sopclans.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class LeaveCommand {
	/*
	 * Usage: /clan leave
	 */
	public LeaveCommand(CommandSender sender) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}
		
		Player leaver = (Player)sender;
		Clan clan = SopClans.cm.getClan(leaver);
		if(clan == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(leaver));
			return;
		}
		
		Member member = clan.getMember(leaver);
		if(clan.getLeader() == member) {
			leaver.sendMessage(Message.CANNOT_LEAVE_BECAUSE_LEADER.getMessageWithPlaceholders(leaver));
			return;
		}
		
		if(!clan.removeMember(member)) {
			leaver.sendMessage(Message.LEAVE_ERROR.getMessageWithPlaceholders(leaver));
			return;
		}
		
		String message = Message.LEAVE_BROADCAST_1.getMessageWithPlaceholders(leaver).replaceAll("%player%", leaver.getName());
		message += " " + Message.LEAVE_BROADCAST_2.getMessage();
		clan.broadcastMembers(message);
		leaver.sendMessage(Message.LEAVE.getMessageWithPlaceholders(leaver));
	}
}

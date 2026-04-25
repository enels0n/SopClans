package net.enelson.sopclans.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class HelpCommand {
	public HelpCommand(CommandSender sender) {
		boolean isPlayer = false;
		boolean inClan = false;
		boolean isLeader = false;
		Clan clan = null;
		Player player = null;
		Member member = null;
		String message = "";
		
		if(sender instanceof Player) {
			isPlayer = true;
			player = (Player)sender;
			clan = SopClans.cm.getClan(player);
			if(clan != null) {
				inClan = true;
				member = clan.getMember(player);
				if(clan.getLeader() == member) {
					isLeader = true;
				}
			}
		}

		message += Message.HELP_TITLE.getMessageWithPlaceholders(player)+"\n";
		message += Message.HELP_LIST.getMessageWithPlaceholders(player)+"\n";
		
		if(isPlayer) {
			if(inClan) {
				message += Message.HELP_MEMBERS.getMessageWithPlaceholders(player)+"\n";
				message += Message.HELP_HOME.getMessageWithPlaceholders(player)+"\n";
				if(isLeader) {
					message += Message.HELP_TRANSFER.getMessageWithPlaceholders(player)+"\n";
					message += Message.HELP_DELETE.getMessageWithPlaceholders(player)+"\n";
				}
				if(isLeader || member.getRank().getPermission("can_change_name"))
					message += Message.HELP_NAME.getMessageWithPlaceholders(player)+"\n";
				if(isLeader || member.getRank().getPermission("can_change_tag"))
					message += Message.HELP_TAG.getMessageWithPlaceholders(player)+"\n";
				if(isLeader || member.getRank().getPermission("can_invite"))
					message += Message.HELP_INVITE.getMessageWithPlaceholders(player)+"\n";
				if(isLeader || member.getRank().getPermission("can_kick"))
					message += Message.HELP_KICK.getMessageWithPlaceholders(player)+"\n";
				if(isLeader || member.getRank().getPermission("can_promote"))
					message += Message.HELP_PROMOTE.getMessageWithPlaceholders(player)+"\n";
				if(isLeader || member.getRank().getPermission("can_demote"))
					message += Message.HELP_DEMOTE.getMessageWithPlaceholders(player)+"\n";
				if(isLeader || member.getRank().getPermission("can_addslots"))
					message += Message.HELP_ADDSLOTS.getMessageWithPlaceholders(player)+"\n";
				if(isLeader || member.getRank().getPermission("can_declare_war"))
					message += Message.HELP_DECLARE_WAR.getMessageWithPlaceholders(player)+"\n";
				if(isLeader || member.getRank().getPermission("can_set_clanhome"))
					message += Message.HELP_SET_CLANHOME.getMessageWithPlaceholders(player)+"\n";
				if(!isLeader)
					message += Message.HELP_LEAVE.getMessageWithPlaceholders(player)+"\n";
			}
			else {
				message += Message.HELP_CREATE.getMessageWithPlaceholders(player)+"\n";
				message += Message.HELP_INVITES.getMessageWithPlaceholders(player)+"\n";
				message += Message.HELP_ACCEPT.getMessageWithPlaceholders(player)+"\n";
				message += Message.HELP_DENY.getMessageWithPlaceholders(player)+"\n";
			}
		}

		message += Message.HELP_FOOTER.getMessageWithPlaceholders(player);
		sender.sendMessage(message);
	}
}

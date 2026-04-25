package net.enelson.sopclans.commands.subcommands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.invite.Invite;
import net.enelson.sopclans.utils.Message;

public class AcceptCommand {
	/* 
	 * Usage: /clan accept <clanId>
	 * Usage: /clan deny <clanId>
	 */
	public AcceptCommand(CommandSender sender, String[] args, boolean accept) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getDefautMessage());
			return;
		}

		Player player = (Player)sender;
		
		if(args.length != 1) {
			sender.sendMessage(Message.INVALID_ARGUMENTS.getMessageWithPlaceholders(player));
			return;
		}
		
		int clanId = Integer.parseInt(args[0]);
		if(clanId == 0) {
			sender.sendMessage(Message.INVALID_ID.getMessageWithPlaceholders(player));
			sender.sendMessage(Message.HELP_INVITES.getMessageWithPlaceholders(player));
			return;
		}
		
		Invite invite = SopClans.im.getInvite(clanId, player.getUniqueId().toString());
		if(invite == null) {
			sender.sendMessage(Message.INVITE_NOT_FOUND.getMessageWithPlaceholders(player));
			sender.sendMessage(Message.HELP_INVITES.getMessageWithPlaceholders(player));
			return;
		}
		
		Clan clan = SopClans.cm.getClan(clanId);
		if(clan == null) {
			sender.sendMessage(Message.SOMETHING_WRONG_CLAN_NOT_FOUND.getMessageWithPlaceholders(player));
			return;
		}
		
		String message = "";
		if(accept) {
			clan.addMember(player);
			message = Message.ACCEPT_INVITE_1.getMessageWithPlaceholders(player)
					.replaceAll("%player%", player.getName());
			message += " " + Message.ACCEPT_INVITE_2.getMessage();
		}
		else {
			message = Message.DENY_INVITE_1.getMessageWithPlaceholders(player)
					.replaceAll("%player%", player.getName());
			message += " " + Message.DENY_INVITE_2.getMessage();
			if(SopClans.economy != null) {
				Double refund = SopClans.configMain.getDouble("economy.invite_refund");
				OfflinePlayer inviter = Bukkit.getOfflinePlayer(UUID.fromString(invite.getInviterUuid()));
				if(inviter.isOnline()) {
					((Player)inviter).sendMessage(Message.DENY_REFUND.getMessageWithPlaceholders((Player)inviter).replaceAll("%refund%", refund+""));
				}
				SopClans.economy.depositPlayer(inviter, refund);
			}
		}
		
		clan.broadcastMembers(message);
		SopClans.im.removeInvite(invite);
	}
}

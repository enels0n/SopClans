package net.enelson.sopclans.commands.subcommands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.invite.Invite;
import net.enelson.sopclans.utils.Message;
import net.enelson.sopclans.utils.Utils;

public class InvitesCommand {
	/*
	 * Usage: /clan invites
	 */
	public InvitesCommand(CommandSender sender) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}

		Player player = (Player)sender;
		List<Invite> invites = SopClans.im.getInvites(player);
		
		if(invites.isEmpty()) {
			player.sendMessage(Message.INVITES_NO_INVITES.getMessageWithPlaceholders(player));
			return;
		}
		
		player.sendMessage(Message.INVITES_LIST_TITLE.getMessageWithPlaceholders(player));
		for(Invite invite : invites) {
			Clan clan = SopClans.cm.getClan(invite.getClanId());
			String message = Message.INVITES_LIST_ITEM_1.getMessageWithPlaceholders(player)
					.replaceAll("%id%", invite.getClanId()+"")
					.replaceAll("%clan%", clan.getName())
					.replaceAll("%tag%", clan.getTag())
					.replaceAll("%color%", Utils.getColor(clan.getLevel()))
					.replaceAll("%rank%", clan.getMember(invite.getInviterName()).getRank().getDisplayName())
					.replaceAll("%inviter%", invite.getInviterName());
			
			Player p = Bukkit.getPlayerExact(invite.getInviterName());
			if(p != null) {
				message += Message.INVITES_LIST_ITEM_2.getMessageWithPlaceholders(p)
						.replaceAll("%id%", invite.getClanId()+"")
						.replaceAll("%clan%", clan.getName())
						.replaceAll("%tag%", clan.getTag())
						.replaceAll("%color%", Utils.getColor(clan.getLevel()))
						.replaceAll("%rank%", ChatColor.translateAlternateColorCodes('&', clan.getMember(invite.getInviterName()).getRank().getDisplayName()))
						.replaceAll("%inviter%", invite.getInviterName());
			}
			else {
				message += Message.INVITES_LIST_ITEM_2_INVITER_OFFLINE.getMessageWithPlaceholders(player)
						.replaceAll("%id%", invite.getClanId()+"")
						.replaceAll("%clan%", clan.getName())
						.replaceAll("%tag%", clan.getTag())
						.replaceAll("%color%", Utils.getColor(clan.getLevel()))
						.replaceAll("%rank%", ChatColor.translateAlternateColorCodes('&', clan.getMember(invite.getInviterName()).getRank().getDisplayName()))
						.replaceAll("%inviter%", invite.getInviterName());
			}
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
		player.sendMessage(Message.INVITES_LIST_FOOTER.getMessageWithPlaceholders(player));
	}
}

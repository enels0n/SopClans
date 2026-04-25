package net.enelson.sopclans.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.utils.Message;

public class DeleteCommand {
	/*
	 * Usage: /clan delete
	 */
	public DeleteCommand(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}

		Player player = (Player) sender;
		Clan clan = SopClans.cm.getClan(player);
		if (clan == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(player));
			return;
		}

		if (!clan.getLeader().getUniqueId().equals(player.getUniqueId().toString())) {
			player.sendMessage(Message.IS_NOT_LEADER.getMessageWithPlaceholders(player));
			return;
		}

		SopClans.cm.removeClan(clan);
		clan.broadcastMembers(Message.CLAN_DELETE_BROADCAST.getMessage());
		player.sendMessage(Message.CLAN_DELETE.getMessageWithPlaceholders(player).replaceAll("%id%", clan.getId() + ""));
	}
}

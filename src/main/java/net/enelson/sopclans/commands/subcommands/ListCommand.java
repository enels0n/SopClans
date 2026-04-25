package net.enelson.sopclans.commands.subcommands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.utils.Message;

public class ListCommand {
	/*
	 * Usage: /clan list
	 */
	public ListCommand(CommandSender sender) {
		List<Clan> clans = SopClans.cm.getClans();
		if(clans.size() == 0) {
			if(sender instanceof Player)
				sender.sendMessage(Message.CLANLIST_EMPTY.getMessageWithPlaceholders((Player)sender));
			else
				sender.sendMessage(Message.CLANLIST_EMPTY.getDefautMessage());
			return;
		}

		if(sender instanceof Player)
			sender.sendMessage(Message.CLANLIST_TITLE.getMessageWithPlaceholders((Player)sender));
		else
			sender.sendMessage(Message.CLANLIST_TITLE.getDefautMessage());
		
		for(Clan clan : clans) {
			Player player = Bukkit.getPlayerExact(clan.getLeader().getPlayerName());
			String message = "";
			
			if(sender instanceof Player)
				message += " " + Message.CLANLIST_ITEM_1.getMessageWithPlaceholders((Player)sender);
			else
				message += " " + Message.CLANLIST_ITEM_1.getDefautMessage();
			
			message = message
					.replaceAll("%id%", clan.getId()+"")
					.replaceAll("%name%", clan.getName())
					.replaceAll("%tag%", clan.getTag());
			
			if(player != null) {
				message += " " + Message.CLANLIST_ITEM_2.getMessageWithPlaceholders(player).replaceAll("%leader%", player.getName());
			}
			else {
				message += " " + Message.CLANLIST_ITEM_2_OFFLINE_LEADER.getMessage().replaceAll("%leader%", clan.getLeader().getPlayerName());
			}
			
			sender.sendMessage(message);
		}
	}
}

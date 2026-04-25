package net.enelson.sopclans.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.utils.Message;

public class HomeCommand {
	
	public HomeCommand(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}
		
		Player player = (Player)sender;
		Clan clan = SopClans.cm.getClan(player);
		if(clan == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(player));
			return;
		}

		if(clan.getClanhome() == null) {
			sender.sendMessage(Message.CLANHOME_EMPTY.getMessageWithPlaceholders(player));
			return;
		}

		player.teleport(clan.getClanhome());
		player.sendMessage(Message.CLANHOME_TELEPORT.getMessageWithPlaceholders(player));
	}
}

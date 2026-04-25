package net.enelson.sopclans.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class SethomeCommand {
	
	public SethomeCommand(CommandSender sender) {
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
		
		Member member = clan.getMember(player);
		if(!member.getRank().getPermission("can_set_clanhome")) {
			player.sendMessage(Message.NO_ACCESS_SETHOME.getMessageWithPlaceholders(player));
			return;
		}

		Double price = SopClans.configMain.getDouble("economy.set_clanhome");
		if(SopClans.economy != null && price>0) {
			if(SopClans.economy.getBalance(player) < price) {
				player.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
				return;
			}
			
			player.sendMessage(Message.CHARGED_CLANHOME_SET.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
			SopClans.economy.withdrawPlayer(player, price);
		}
		
		String message = Message.CLANHOME_SET_1.getMessageWithPlaceholders(player)
				.replaceAll("%rank%", member.getRank().getDisplayName())
				.replaceAll("%player%", player.getName());
		
		message += Message.CLANHOME_SET_2.getMessage()
				.replaceAll("%world%", player.getLocation().getWorld().getName())
				.replaceAll("%x%", player.getLocation().getBlockX()+"")
				.replaceAll("%y%", player.getLocation().getBlockY()+"")
				.replaceAll("%z%", player.getLocation().getBlockZ()+"");
		
		clan.setClanhome(player.getLocation());
		clan.broadcastMembers(message);
	}
}

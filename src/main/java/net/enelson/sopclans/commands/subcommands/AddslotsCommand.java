package net.enelson.sopclans.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class AddslotsCommand {
	
	public AddslotsCommand(CommandSender sender) {
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
		if(!member.getRank().getPermission("can_add_slots")) {
			player.sendMessage(Message.NO_ACCESS_ADD_SLOTS.getMessageWithPlaceholders(player));
			return;
		}
		
		if(clan.getSlotCount()>=SopClans.configMain.getInt("clan.max_slots")) {
			player.sendMessage(Message.MAX_SLOTS.getMessageWithPlaceholders(player));
			return;
		}

		Double price = SopClans.configMain.getDouble("economy.add_slots_price");
		if(SopClans.economy != null && !clan.getTag().equals("") && price>0) {
			if(SopClans.economy.getBalance(player) < price) {
				player.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
				return;
			}
			
			player.sendMessage(Message.CHARGED_ADD_SLOTS.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
			SopClans.economy.withdrawPlayer(player, price);
		}
		
		
		String message = Message.ADD_SLOTS_1.getMessageWithPlaceholders(player)
				.replaceAll("%rank%", member.getRank().getDisplayName())
				.replaceAll("%player%", player.getName());
		
		message += " " + Message.ADD_SLOTS_2.getMessage()
				.replaceAll("%oldSlots%", clan.getSlotCount()+"")
				.replaceAll("%newSlots%", (clan.getSlotCount()+SopClans.configMain.getInt("clan.add_slots_step"))+"");
		
		clan.addSlotCount(SopClans.configMain.getInt("clan.add_slots_step"));
		clan.broadcastMembers(ChatColor.translateAlternateColorCodes('&', message));
	}
}

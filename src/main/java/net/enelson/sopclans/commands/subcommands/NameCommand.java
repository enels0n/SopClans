package net.enelson.sopclans.commands.subcommands;

import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;
import net.enelson.sopclans.utils.Utils;

public class NameCommand {
	
	public NameCommand(CommandSender sender, String[] args) {
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
		
		Member member = clan.getMember(player);
		if(!member.getRank().getPermission("can_change_name")) {
			player.sendMessage(Message.NO_ACCESS_CHANGE_NAME.getMessageWithPlaceholders(player));
			return;
		}

		if(!(args != null && args.length > 0)) {
			sender.sendMessage(Message.HELP_NAME.getMessageWithPlaceholders(player));
			return;
		}
		
		String clanName = "";
		for(String s : args) {
			clanName += s+" ";
		}
		clanName = clanName.trim();
		
		if(clan.getName().equals(clanName)) {
			player.sendMessage(Message.NAME_ALREADY_SET.getMessageWithPlaceholders(player));
			return;
		}
		
		Pattern pattern = Pattern.compile(SopClans.configMain.getString("clan.name_characters"));
		if(!pattern.matcher(clanName).matches()) {
			sender.sendMessage(Message.INVALID_NAME.getMessageWithPlaceholders(player));
			return;
		}
		
		if(clanName.charAt(0) == '_' || clanName.charAt(clanName.length()-1) == '_') {
			sender.sendMessage(Message.INVALID_NAME_1.getMessageWithPlaceholders(player));
			return;
		}
		
		if(SopClans.cm.getClanByName(clanName) != null) {
			player.sendMessage(Message.ANOTHER_CLAN_HAS_THIS_NAME.getMessageWithPlaceholders(player));
			return;
		}
		
		Double price = SopClans.configMain.getDouble("economy.change_name_price");
		if(SopClans.economy != null && price>0) {
			if(SopClans.economy.getBalance(player) < price) {
				player.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
				return;
			}
			
			player.sendMessage(Message.CHARGED_CHANGE_NAME.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
			SopClans.economy.withdrawPlayer(player, price);
		}
		
		clan.setName(clanName);
		String message = Message.SET_NAME_1.getMessageWithPlaceholders(player)
				.replaceAll("%rank%", member.getRank().getDisplayName())
				.replaceAll("%player%", player.getName());
		message += " " + Message.SET_NAME_2.getMessage().replaceAll("%name%", Utils.getColor(clan.getLevel())+clan.getName());
		clan.broadcastMembers(message);
	}
}

package net.enelson.sopclans.commands.subcommands;

import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.utils.Message;

public class CreateCommand {
	/* 
	 * Usage: /clan create <clanName>
	 */
	public CreateCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}
		
		Player player = (Player)sender;
		if(SopClans.cm.getClan(player) != null) {
			player.sendMessage(Message.CREATE_ALREADY_IN_CLAN.getMessageWithPlaceholders(player));
			return;
		}
		
		Double price = SopClans.configMain.getDouble("economy.create_price");
		if(SopClans.economy != null) {
			if(SopClans.economy.getBalance(player) < price) {
				player.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
				return;
			}
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
		
		if(SopClans.economy != null && price>0) {
			player.sendMessage(Message.CHARGED_CREATE.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
			SopClans.economy.withdrawPlayer(player, price);
		}
		
		Clan clan = SopClans.cm.createClan(player, clanName);

		player.sendMessage(Message.CREATE_CLAN.getMessageWithPlaceholders(player).replaceAll("%id%", clan.getId()+""));
		
	}
}

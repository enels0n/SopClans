package net.enelson.sopclans.commands.subcommands;

import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;
import net.enelson.sopclans.utils.Utils;

public class TagCommand {
	
	public TagCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}

		Player player = (Player)sender;
		
		if(args.length != 1) {
			sender.sendMessage(Message.INVALID_ARGUMENTS.getMessageWithPlaceholders(player));
			return;
		}
		
		Clan clan = SopClans.cm.getClan(player);
		if(clan == null) {
			player.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(player));
			return;
		}
		
		Member member = clan.getMember(player);
		if(!member.getRank().getPermission("can_change_tag")) {
			player.sendMessage(Message.NO_ACCESS_CHANGE_TAG.getMessageWithPlaceholders(player));
			return;
		}
		
		String newTag = args[0];
		if(clan.getTag().equals(newTag)) {
			player.sendMessage(Message.TAG_ALREADY_SET.getMessageWithPlaceholders(player));
			return;
		}
		
		Pattern pattern = Pattern.compile(SopClans.configMain.getString("clan.tag_characters"));
		if(!pattern.matcher(newTag).matches()) {
			sender.sendMessage(Message.INVALID_TAG.getMessageWithPlaceholders(player));
			return;
		}
		
		if(newTag.charAt(0) == '_' || newTag.charAt(newTag.length()-1) == '_') {
			sender.sendMessage(Message.INVALID_TAG_1.getMessageWithPlaceholders(player));
			return;
		}
		
		if(SopClans.cm.getClanByTag(newTag) != null) {
			player.sendMessage(Message.ANOTHER_CLAN_HAS_THIS_TAG.getMessageWithPlaceholders(player));
			return;
		}
		
		Double price = SopClans.configMain.getDouble("economy.change_tag_price");
		if(SopClans.economy != null && !clan.getTag().equals("") && price>0) {
			if(SopClans.economy.getBalance(player) < price) {
				player.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
				return;
			}
			
			player.sendMessage(Message.CHARGED_SET_TAG.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
			SopClans.economy.withdrawPlayer(player, price);
		}
		
		clan.setTag(newTag);
		String message = Message.SET_TAG_1.getMessageWithPlaceholders(player)
				.replaceAll("%rank%", member.getRank().getDisplayName())
				.replaceAll("%player%", player.getName());
		message += " " + Message.SET_TAG_2.getMessage().replaceAll("%tag%", Utils.getColor(clan.getLevel())+clan.getTag());
		clan.broadcastMembers(message);
	}
}

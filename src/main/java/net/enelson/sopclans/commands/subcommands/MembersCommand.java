package net.enelson.sopclans.commands.subcommands;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class MembersCommand {
	/*
	 * Usage: /clan members
	 */
	public MembersCommand(CommandSender sender) {
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

		sender.sendMessage(Message.MEMBER_LIST.getMessageWithPlaceholders(player)
				.replaceAll("%count%", clan.getMembersCount()+"")
				.replaceAll("%slots%", clan.getSlotCount()+""));
		
		List<Member> members = clan.getMembers();
		members.sort(Comparator.comparing(m -> SopClans.rm.getRanks().indexOf(m.getRank())));
		
		for(Member member : members) {
			String message = "";
			Player p = Bukkit.getPlayerExact(member.getPlayerName());
			if(p != null) {
				message = Message.MEMBER_LIST_ITEM.getMessageWithPlaceholders(p);
			}
			else {
				message = Message.MEMBER_LIST_ITEM_OFFLINE.getMessage();
			}
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message
					.replaceAll("%rank%", member.getRank().getDisplayName())
					.replaceAll("%player%", member.getPlayerName())));
		}
	}
}

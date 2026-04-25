package net.enelson.sopclans.commands.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.invite.Invite;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class InviteCommand {
	/*
	 * Usage: /clan invite <player>
	 */
	public InviteCommand(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}

		Player inviter = (Player)sender;
		
		if(args.length != 1) {
			sender.sendMessage(Message.INVALID_ARGUMENTS.getMessageWithPlaceholders(inviter));
			return;
		}
		
		Clan clan = SopClans.cm.getClan(inviter);
		if(clan == null) {
			sender.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(inviter));
			return;
		}
		
		Member member = clan.getMember(inviter);
		if(!member.getRank().getPermission("can_invite")) {
			inviter.sendMessage(Message.NO_ACCESS_INVITE.getMessageWithPlaceholders(inviter));
			return;
		}
		Double price = SopClans.configMain.getDouble("economy.invite_price");
		if(SopClans.economy != null && price>0) {
			if(SopClans.economy.getBalance(inviter) < price) {
				inviter.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(inviter).replaceAll("%price%", price+""));
				return;
			}
		}
		
		Player invited = Bukkit.getPlayerExact(args[0]);
		if(invited == null) {
			inviter.sendMessage(Message.INVITE_ONLY_ONLINE_PLAYER.getMessageWithPlaceholders(invited));
			return;
		}
		
		if(SopClans.cm.getClan(invited) != null) {
			inviter.sendMessage(Message.INVITE_PLAYER_IS_IN_CLAN.getMessageWithPlaceholders(invited));
			return;
		}
		
		Invite invite = SopClans.im.getInvite(clan.getId(), invited.getUniqueId().toString());
		if(invite != null) {
			inviter.sendMessage(Message.INVITE_PLAYER_ALREADY_INVITED.getMessageWithPlaceholders(invited));
			return;
		}
		
		if(clan.getSlotCount() <= clan.getMembersCount()+SopClans.im.getClanInvitesCount(clan.getId())) {
			inviter.sendMessage(Message.INVITE_MAX_MEMBERS.getMessageWithPlaceholders(invited)
					.replaceAll("%members%", clan.getMembersCount()+"")
					.replaceAll("%slots%", clan.getSlotCount()+"")
					.replaceAll("%invites%", SopClans.im.getClanInvitesCount(clan.getId())+""));
			return;
		}
		
		if(SopClans.economy != null && price>0) {
			inviter.sendMessage(Message.CHARGED_INVITE.getMessageWithPlaceholders(invited).replaceAll("%price%", price+""));
			SopClans.economy.withdrawPlayer(inviter, price);
		}
		
		invite = SopClans.im.createInvite(clan.getId(), inviter, invited, System.currentTimeMillis()/1000);
		
		
		String message = Message.INVITE_1.getMessageWithPlaceholders(invited)
				.replaceAll("%rank%", member.getRank().getDisplayName())
				.replaceAll("%player%", inviter.getName());

		message += " " + Message.INVITE_2.getMessage();
		
		message += " " + Message.INVITE_3.getMessageWithPlaceholders(invited)
				.replaceAll("%player%", invited.getName());
		
		clan.broadcastMembers(ChatColor.translateAlternateColorCodes('&', message));
		
		message = Message.INVITE_INVITED_1.getMessageWithPlaceholders(inviter);
		message += " " + Message.INVITE_INVITED_2.getMessageWithPlaceholders(invited)
			.replaceAll("%clan%", clan.getName())
			.replaceAll("%tag%", clan.getTag())
			.replaceAll("%id%", clan.getId()+"");
		
		invited.sendMessage(message);
	}
}

package net.enelson.sopclans.commands.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.data.member.Member;
import net.enelson.sopclans.utils.Message;

public class PvpCommand {

	public PvpCommand(CommandSender sender) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Message.ONLY_PLAYER.getMessage());
			return;
		}
		
		Player player = (Player)sender;
		Clan clan = SopClans.cm.getClan(player);
		if(clan == null) {
			player.sendMessage(Message.NOT_IN_CLAN.getMessageWithPlaceholders(player));
			return;
		}
		
		Member member = clan.getMember(player);
		if(!member.getRank().getPermission("can_pvp_switch")) {
			player.sendMessage(Message.NO_ACCESS_SWITCH_PVP.getMessageWithPlaceholders(player));
			return;
		}
		
		Double price = SopClans.configMain.getDouble("economy.pvp_switch_price");
		if(SopClans.economy != null) {
			if(SopClans.economy.getBalance(player) < price) {
				player.sendMessage(Message.NOT_ENOUGH_MONEY.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
				return;
			}

			player.sendMessage(Message.CHARGED_SWITCH_PVP.getMessageWithPlaceholders(player).replaceAll("%price%", price+""));
			SopClans.economy.withdrawPlayer(player, price);
		}
		
		clan.switchPvp();
		String messsage = "";
		if(clan.isPvpEnabled()) {
			messsage = Message.SWITCH_PVP_ON_1.getMessage()
					.replaceAll("%player%", player.getName())
					.replaceAll("%rank%", member.getRank().getDisplayName());
			messsage += Message.SWITCH_PVP_ON_2.getMessageWithPlaceholders(player)
					.replaceAll("%player%", player.getName())
					.replaceAll("%rank%", member.getRank().getDisplayName());
			
		}
		else {
			messsage = Message.SWITCH_PVP_OFF_1.getMessage()
					.replaceAll("%player%", player.getName())
					.replaceAll("%rank%", member.getRank().getDisplayName());
			messsage += Message.SWITCH_PVP_OFF_2.getMessageWithPlaceholders(player)
					.replaceAll("%player%", player.getName())
					.replaceAll("%rank%", member.getRank().getDisplayName());
		}
		
		
		clan.broadcastMembers(messsage);
	}
}

package net.enelson.sopclans.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.enelson.sopclans.commands.subcommands.AcceptCommand;
import net.enelson.sopclans.commands.subcommands.AddslotsCommand;
import net.enelson.sopclans.commands.subcommands.CreateCommand;
import net.enelson.sopclans.commands.subcommands.DeleteCommand;
import net.enelson.sopclans.commands.subcommands.DemoteCommand;
import net.enelson.sopclans.commands.subcommands.HelpCommand;
import net.enelson.sopclans.commands.subcommands.HomeCommand;
import net.enelson.sopclans.commands.subcommands.InviteCommand;
import net.enelson.sopclans.commands.subcommands.InvitesCommand;
import net.enelson.sopclans.commands.subcommands.KickCommand;
import net.enelson.sopclans.commands.subcommands.LeaveCommand;
import net.enelson.sopclans.commands.subcommands.ListCommand;
import net.enelson.sopclans.commands.subcommands.MembersCommand;
import net.enelson.sopclans.commands.subcommands.NameCommand;
import net.enelson.sopclans.commands.subcommands.PromoteCommand;
import net.enelson.sopclans.commands.subcommands.PvpCommand;
import net.enelson.sopclans.commands.subcommands.SethomeCommand;
import net.enelson.sopclans.commands.subcommands.TagCommand;
import net.enelson.sopclans.commands.subcommands.TransferCommand;
import net.enelson.sopclans.commands.subcommands.WarCommand;
import net.enelson.sopclans.utils.Message;

public class CommandManager implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			if(sender instanceof Player)
				sender.sendMessage(Message.HELP_HELP.getMessageWithPlaceholders((Player)sender));
			else
				sender.sendMessage(Message.HELP_HELP.getMessage());
			return false;
		}

		if (args[0].equalsIgnoreCase("create"))
			new CreateCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("delete"))
			new DeleteCommand(sender);
		else if (args[0].equalsIgnoreCase("transfer"))
			new TransferCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("name"))
			new NameCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("tag"))
			new TagCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("invite"))
			new InviteCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("accept"))
			new AcceptCommand(sender, this.removeElement(args, 0), true);
		else if (args[0].equalsIgnoreCase("deny"))
			new AcceptCommand(sender, this.removeElement(args, 0), false);
		else if (args[0].equalsIgnoreCase("kick"))
			new KickCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("promote"))
			new PromoteCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("demote"))
			new DemoteCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("war"))
			new WarCommand(sender, this.removeElement(args, 0));
		else if (args[0].equalsIgnoreCase("leave"))
			new LeaveCommand(sender);
		else if (args[0].equalsIgnoreCase("list"))
			new ListCommand(sender);
		else if (args[0].equalsIgnoreCase("invites"))
			new InvitesCommand(sender);
		else if (args[0].equalsIgnoreCase("members"))
			new MembersCommand(sender);
		else if (args[0].equalsIgnoreCase("addslots"))
			new AddslotsCommand(sender);
		else if (args[0].equalsIgnoreCase("pvp"))
			new PvpCommand(sender);
		else if (args[0].equalsIgnoreCase("help"))
			new HelpCommand(sender);
		else if (args[0].equalsIgnoreCase("sethome"))
			new SethomeCommand(sender);
		else if (args[0].equalsIgnoreCase("home"))
			new HomeCommand(sender);
		else {
			if(sender instanceof Player)
				sender.sendMessage(Message.HELP_HELP.getMessageWithPlaceholders((Player)sender));
			else
				sender.sendMessage(Message.HELP_HELP.getDefautMessage());
		}
		
		return true;
	}

	private String[] removeElement(String[] arr, int index) {
		String[] copyArray = new String[arr.length - 1];
		System.arraycopy(arr, 0, copyArray, 0, index);
		System.arraycopy(arr, index + 1, copyArray, index, arr.length - index - 1);
		return copyArray;
	}
}

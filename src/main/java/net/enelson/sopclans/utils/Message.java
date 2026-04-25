package net.enelson.sopclans.utils;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.enelson.sopclans.SopClans;

public enum Message {

	ONLY_PLAYER("messages.only_player", "The command can only be used by a player."),
	INVALID_ARGUMENTS("messages.invalid_arguments", "Invalid number of arguments"),
	NOT_IN_CLAN("messages.not_in_clan", "You are not a member of a clan."),
	INVALID_ID("messages.invalid_id", "Invalid clan id"),
	CLAN_NOT_FOUND("messages.clan_not_found", "There is no clan with such ID."),
	IS_NOT_LEADER("messages.is_not_leader", "You are not the leader of this clan."),
	CLAN_DELETE_BROADCAST("messages.clan_delete_broadcast", "Your clan has been disbanded."),
	CLAN_DELETE("messages.clan_delete", "The clan with ID %id% has been disbanded."),
	MAX_SLOTS("messages.max_slots", "The maximum number of slots in the clan has been reached."),
	NO_MEMBER_FOUND("messages.no_member_found", "There is no such player in the clan."),
	NOT_ENOUGH_MONEY("messages.not_enought_money", "You don't have enough money (%price%)"),
	CANNOT_DECLARE_YOUSELF("messages.cannot_declare_youself", "You cannot declare war on yourself."),
	CLAN_IS_ALREADY_WAR("messages.clan_is_already_war", "There is already a war going on between your clans."),
	CHARGED_WAR("messages.charged_war", "You have been charged $%price% for declaring war."),
	CHARGED_PROMOTE("messages.charged_promote", "You have been charged $%price% for promoting a player."),
	CHARGED_DEMOTE("messages.charged_demote", "You have been charged $%price% for demoting a player."),
	CHARGED_SWITCH_PVP("messages.charged_switch_pvp", "You have been charged $%price% for switching PVP mode."),
	CHARGED_SET_TAG("messages.charged_set_tag", "You have been charged $%price% for changing the tag."),
	CHARGED_ADD_SLOTS("messages.charged_add_slots", "You have been charged $%price% for adding slots."),
	CHARGED_CHANGE_NAME("messages.charged_change_name", "You have been charged $%price% for changing the name."),
	CHARGED_KICK("messages.charged_kick", "You have been charged $%price% for kicking a player from a clan."),
	CHARGED_INVITE("messages.charged_invite", "You have been charged $%price% for an invitation to the clan."),
	CHARGED_CREATE("messages.charged_creative", "You have been charged $%price% for creating a clan."),
	CHARGED_CLANHOME_SET("messages.charged_clanhome_set", "You have been charged $%price% for setting up a clan house point."),
	INVITE_NOT_FOUND("messages.invite_not_found", "The invite is not found."),
	SOMETHING_WRONG_CLAN_NOT_FOUND("messages.something_wrong_clan_not_found", "Something went wrong - clan not found."),
	SWITCH_PVP_ON_1("messages.switch_pvp_on_1", "Intra-clan PVP mode was enabled by "),
	SWITCH_PVP_ON_2("messages.switch_pvp_on_2", "[%rank%] %player%"),
	SWITCH_PVP_OFF_1("messages.switch_pvp_off_1", "Intra-clan PVP mode was disabled by "),
	SWITCH_PVP_OFF_2("messages.switch_pvp_off_2", "[%rank%] %player%"),
	ACCEPT_INVITE_1("messages.accept_invite_1", "%player%"),
	ACCEPT_INVITE_2("messages.accept_invite_2", "has joined the clan."),
	DENY_INVITE_1("messages.deny_invite_1", "%player%"),
	DENY_INVITE_2("messages.deny_invite_2", "has declined the invitation to the clan."),
	DENY_REFUND("messages.deny_refund", "You were refunded %refund% for the declined invitation."),
	TAG_ALREADY_SET("messages.tag_already_set", "This tag is already set."),
	INVALID_TAG("messages.invalid_tag", "Invalid tag. Only Latin or Cyrillic characters, numbers or the _ symbol can be used. Size from 3 to 10 characters."),
	INVALID_TAG_1("messages.invalid_tag_1", "Tag cannot start or end with '_'"),
	ANOTHER_CLAN_HAS_THIS_TAG("messages.another_clan_has_this_tag", "A clan with this tag already exists."),
	SET_TAG_1("messages.set_tag_1", "[%rank%&r] %player%"),
	SET_TAG_2("messages.set_tag_2", "has changed clan tag - [%tag%]"),
	DEMOTE_LOWER_RANK("messages.demote_lower_rank", "You can only demote players with a rank lower than yours."),
	PLAYER_CANNOT_DEMOTED("messages.player_cannot_demoted", "This player cannot be demoted."),
	DEMOTE_PLAYER_1("messages.demote_player_1", "[%rank%] %player%"),
	DEMOTE_PLAYER_2("messages.demote_player_2", "has demoted"),
	DEMOTE_PLAYER_3("messages.demote_player_3", "%player%"),
	DEMOTE_PLAYER_4("messages.demote_player_4", "to rank [%rank%]"),
	PROMOTE_PLAYER_1("messages.promote_player_1", "[%rank%] %player%"),
	PROMOTE_PLAYER_2("messages.promote_player_2", "has promoted"),
	PROMOTE_PLAYER_3("messages.promote_player_3", "%player%"),
	PROMOTE_PLAYER_4("messages.promote_player_4", "to rank [%rank%]"),
	ADD_SLOTS_1("messages.add_slots_1", "[%rank%] %player%"),
	ADD_SLOTS_2("messages.add_slots_2", "purchased additional slots for the clan. (%oldSlots% -> %newSlots%)"),
	TRANSFER_CLAN_1("messages.transfer_clan_1", "The clan was transferred to a new owner -"),
	TRANSFER_CLAN_2("messages.transfer_clan_2", "%player%"),
	TRANSFER_CLAN_OLD_LEADER("messages.transfer_clan_old_leader", "You have been assigned the former rank of the new owner - %rank%"),
	MEMBER_LIST("messages.member_list", "List of clan membersР вЂ“"),
	MEMBER_LIST_ITEM("messages.member_list_item", "[%rank%] %player%"),
	MEMBER_LIST_ITEM_OFFLINE("messages.member_list_item_offline", "[%rank%] %player%"),
	DECLARE_WAR_ATTACKER_1("messages.declare_war_attaker_1", "[%rank%] %player%"),
	DECLARE_WAR_ATTACKER_2("messages.declare_war_attaker_2", "has declared war on the clan %defender%"),
	DECLARE_WAR_DEFENDER("messages.declare_war_defender", "The %attacker% clan has declared war on you!"),
	CANNOT_ATTACK_CLANMATE("messages.cannot_attack_clanmate", "You cannot attack a clanmate."),
	CLANLIST_EMPTY("messages.clanlist_empty", "The clan list is empty."),
	CLANLIST_TITLE("messages.clanlist_title", "List of clans:"),
	CLANLIST_ITEM_1("messages.clanlist_item_1", "%id% %name% [%tag%] - Leader:"),
	CLANLIST_ITEM_2("messages.clanlist_item_2", "%leader%"),
	CLANLIST_ITEM_2_OFFLINE_LEADER("messages.clanlist_item_2_offline_leader", "%leader%"),
	CANNOT_LEAVE_BECAUSE_LEADER("messages.cannot_leave_because_leader", "You cannot leave because you are the leader of this clan."),
	LEAVE_ERROR("messages.leave_error", "Something went wrong."),
	LEAVE("messages.leave", "message"),
	LEAVE_BROADCAST_1("messages.leave_broadcast_1", "%player%"),
	LEAVE_BROADCAST_2("messages.leave_broadcast_2", "have left the clan."),
	NAME_ALREADY_SET("messages.name_already_set", "This name is already set."),
	INVALID_NAME("messages.invalid_name", "Invalid name. You can use only Latin or Cyrillic characters, numbers, space or _ symbol. Size from 5 to 15 characters."),
	INVALID_NAME_1("messages.invalid_name_1", "Name cannot start or end with '_'"),
	ANOTHER_CLAN_HAS_THIS_NAME("messages.another_clan_has_this_name", "A clan with this name already exists."),
	SET_NAME_1("messages.set_name_1", "[%rank%] %player%"),
	SET_NAME_2("messages.set_name_2", "has changed clan name - [%name%]"),
	KICK_MEMBER_NOT_FOUND("messages.kick_member_not_found", "There is no such player in the clan."),
	KICK_LOWER_RANK("messages.kick_lower_rank", "You can only kick players with a rank lower than yours."),
	KICK_ERROR("messages.kick_error", "Something went wrong."),
	KICK_1("messages.kick_1", "[%rank%] %player%"),
	KICK_2("messages.kick_2", "has kicked"),
	KICK_3("messages.kick_3", "[%rank%] %player%"),
	KICK_4("messages.kick_4", "from the clan."),
	INVITE_ONLY_ONLINE_PLAYER("messages.invite_only_online_player", "You can only invite an online player."),
	INVITE_PLAYER_IS_IN_CLAN("messages.invite_player_is_in_clan", "The player is already a member of a clan."),
	INVITE_PLAYER_ALREADY_INVITED("messages.invite_player_already_invited", "This player has already been invited to the clan."),
	INVITE_MAX_MEMBERS("messages.invite_max_members", "You cannot invite anyone to the clan because the maximum number of members and invites has been reached. To invite, you need to exclude one of the participants or cancel another invitation."),
	INVITE_1("messages.invite_1", "[%rank%] %player%"),
	INVITE_2("messages.invite_2", "has invited to the clan"),
	INVITE_3("messages.invite_3", "%player%"),
	INVITE_INVITED_1("messages.invite_invited_1", "%player%"),
	INVITE_INVITED_2("messages.invite_invited_2", "has invited you to the clan %clan% [%tag%].\n You can run one of the commands:\n /clan accept %id% - to accept\n /clan deny %id% - to deny"),
	CREATE_CLAN("messages.create_clan", "The clan has been created (ID: %id%)"),
	CREATE_ALREADY_IN_CLAN("messages.create_already_in_clan", "You are already a member of a clan."),
	INVITES_NO_INVITES("messages.invites_no_invites", "You do not have any active clan invitations."),
	INVITES_LIST_TITLE("messages.invites_list_title", "Active clan invitations:"),
	INVITES_LIST_ITEM_1("messages.invites_list_item_1", "%id% | %clan% [%tag%] | "),
	INVITES_LIST_ITEM_2("messages.invites_list_item_2", "[%rank%] %inviter%"),
	INVITES_LIST_ITEM_2_INVITER_OFFLINE("messages.invites_list_item_inviter_offline", "[%rank%] %inviter%"),
	INVITES_LIST_FOOTER("messages.invites_list_footer", "Type '/clan accept id' to accept an invite."),
	CLAN_LEVELUP_BROADCAST("messages.clan_levelup_broadcast", "Your clan has increased its level - %level%"),
	CLANHOME_SET_1("messages.clanhome_set_1", "[%rank%] %player%"),
	CLANHOME_SET_2("messages.clanhome_set_2", " has set the clan home point: %world%, %x%, %y%, %z%"),
	CLANHOME_EMPTY("messages.clanhome_empty", "Your clan does not have a clan home point."),
	CLANHOME_TELEPORT("messages.clanhome_teleport", "You have been teleported to the clan home"),
	NO_ACCESS_WAR("messages.no_access_war", "You cannot declare war on other clans."),
	NO_ACCESS_ADD_SLOTS("messages.no_access_add_slots", "You cannot purchase clan slots."),
	NO_ACCESS_DEMOTE("messages.no_access_demote", "You cannot demote members."),
	NO_ACCESS_PROMOTE("messages.no_access_promote", "You cannot promote members."),
	NO_ACCESS_SWITCH_PVP("messages.no_access_switch_pvp", "You cannot switch intra-clan PvP mode."),
	NO_ACCESS_CHANGE_TAG("messages.no_access_change_tag", "You cannot change the tag."),
	NO_ACCESS_CHANGE_NAME("messages.no_access_change_name", "You cannot change the name."),
	NO_ACCESS_KICK("messages.no_access_kick", "You cannot kick members."),
	NO_ACCESS_INVITE("messages.no_access_invite", "You cannot invite new members."),
	NO_ACCESS_SETHOME("messages.no_access_sethome", "You cannot set clan home."),
	HELP_INVITES("help.invites", "&a/clan invites&e - Р С—Р С•РЎРѓР СР С•РЎвЂљРЎР‚Р ВµРЎвЂљРЎРЉ Р С—РЎР‚Р С‘Р С–Р В»Р В°РЎв‚¬Р ВµР Р…Р С‘РЎРЏ"),
	HELP_NAME("help.name", "&a/clan name &c<newName>&e - Р С‘Р В·Р СР ВµР Р…Р С‘РЎвЂљРЎРЉ Р Р…Р В°Р В·Р Р†Р В°Р Р…Р С‘Р Вµ Р С”Р В»Р В°Р Р…Р В°"),
	HELP_HELP("help.help", "&a/clan help&e - Р С—Р С•Р С”Р В°Р В·Р В°РЎвЂљРЎРЉ Р С—Р С•Р СР С•РЎвЂ°РЎРЉ"),
	HELP_MEMBERS("help.members", "&a/clan members&e - РЎРѓР С—Р С‘РЎРѓР С•Р С” РЎС“РЎвЂЎР В°РЎРѓРЎвЂљР Р…Р С‘Р С”Р С•Р Р† Р Р†Р В°РЎв‚¬Р ВµР С–Р С• Р С”Р В»Р В°Р Р…Р В°"),
	HELP_TRANSFER("help.transfer", "&a/clan transfer &c<player>&e - РЎРѓР Т‘Р ВµР В»Р В°РЎвЂљРЎРЉ Р В»Р С‘Р Т‘Р ВµРЎР‚Р С•Р С Р Т‘РЎР‚РЎС“Р С–Р С•Р С–Р С• РЎС“РЎвЂЎР В°РЎРѓРЎвЂљР Р…Р С‘Р С”Р В° Р С”Р В»Р В°Р Р…Р В°"),
	HELP_DELETE("help.delete", "&a/clan delete&e - РЎС“Р Т‘Р В°Р В»Р С‘РЎвЂљРЎРЉ Р С”Р В»Р В°Р Р…"),
	HELP_TAG("help.tag", "&a/clan tag &c<newTag>&e - Р С‘Р В·Р СР ВµР Р…Р С‘РЎвЂљРЎРЉ РЎвЂљР ВµР С– Р С”Р В»Р В°Р Р…Р В°"),
	HELP_INVITE("help.invite", "&a/clan invite &c<player>&e - Р С—РЎР‚Р С‘Р С–Р В»Р В°РЎРѓР С‘РЎвЂљРЎРЉ Р С‘Р С–РЎР‚Р С•Р С”Р В° Р Р† Р С”Р В»Р В°Р Р…"),
	HELP_KICK("help.kick", "&a/clan kick &c<player>&e - Р Р†РЎвЂ№Р С–Р Р…Р В°РЎвЂљРЎРЉ Р С‘Р С–РЎР‚Р С•Р С”Р В° Р С‘Р В· Р С”Р В»Р В°Р Р…Р В°"),
	HELP_PROMOTE("help.promote", "&a/clan promote &c<player>&e - Р С—Р С•Р Р†РЎвЂ№РЎРѓР С‘РЎвЂљРЎРЉ Р В·Р Р†Р В°Р Р…Р С‘Р Вµ Р С‘Р С–РЎР‚Р С•Р С”Р В° Р Р† Р С”Р В»Р В°Р Р…Р Вµ"),
	HELP_DEMOTE("help.demote", "&a/clan demote &c<player>&e - Р С—Р С•Р Р…Р С‘Р В·Р С‘РЎвЂљРЎРЉ Р В·Р Р†Р В°Р Р…Р С‘Р Вµ Р С‘Р С–РЎР‚Р С•Р С”Р В° Р Р† Р С”Р В»Р В°Р Р…Р Вµ"),
	HELP_ADDSLOTS("help.addslots", "&a/clan addslots&e - Р С—РЎР‚Р С‘Р С•Р В±РЎР‚Р ВµРЎРѓРЎвЂљР С‘ Р Т‘Р С•Р С—Р С•Р В»Р Р…Р С‘РЎвЂљР ВµР В»РЎРЉР Р…РЎвЂ№Р Вµ РЎРѓР В»Р С•РЎвЂљРЎвЂ№ Р Т‘Р В»РЎРЏ Р С”Р В»Р В°Р Р…Р В°"),
	HELP_DECLARE_WAR("help.declare_war.", "&a/clan war &c<clanId>&e - Р С•Р В±РЎР‰РЎРЏР Р†Р С‘РЎвЂљРЎРЉ Р Р†Р С•Р в„–Р Р…РЎС“ Р Т‘РЎР‚РЎС“Р С–Р С•Р СРЎС“ Р С”Р В»Р В°Р Р…РЎС“"),
	HELP_LEAVE("help.leave", "&a/clan leave&e - Р С—Р С•Р С”Р С‘Р Р…РЎС“РЎвЂљРЎРЉ Р С”Р В»Р В°Р Р…"),
	HELP_CREATE("help.create", "&a/clan create <Р Р…Р В°Р В·Р Р†Р В°Р Р…Р С‘Р Вµ>&e - РЎРѓР С•Р В·Р Т‘Р В°РЎвЂљРЎРЉ Р С”Р В»Р В°Р Р…"),
	HELP_ACCEPT("help.accept", "&a/clan accept &c<clanId>&e - Р С—РЎР‚Р С‘Р Р…РЎРЏРЎвЂљРЎРЉ Р С—РЎР‚Р С‘Р С–Р В»Р В°РЎв‚¬Р ВµР Р…Р С‘Р Вµ Р С•РЎвЂљ Р С”Р В»Р В°Р Р…Р В° Р Р…Р С•Р СР ВµРЎР‚ clanId"),
	HELP_DENY("help.deny", "&a/clan deny &c<clanId>&e - Р С•РЎвЂљР С”Р В»Р С•Р Р…Р С‘РЎвЂљРЎРЉ Р С—РЎР‚Р С‘Р С–Р В»Р В°РЎв‚¬Р ВµР Р…Р С‘Р Вµ Р С•РЎвЂљ Р С”Р В»Р В°Р Р…Р В° Р Р…Р С•Р СР ВµРЎР‚ clanId"),
	HELP_LIST("help.list", "&a/clan list&e - РЎРѓР С—Р С‘РЎРѓР С•Р С” Р Р†РЎРѓР ВµРЎвЂ¦ Р С”Р В»Р В°Р Р…Р С•Р Р†"),
	HELP_SET_CLANHOME("help.set_clanhome", "&a/clan sethome&e - РЎС“РЎРѓРЎвЂљР В°Р Р…Р С•Р Р†Р С‘РЎвЂљРЎРЉ РЎвЂљР С•РЎвЂЎР С”РЎС“ Р С”Р В»Р В°Р Р…Р С•Р Р†Р С•Р С–Р С• Р Т‘Р С•Р СР В°"),
	HELP_HOME("help.home", "&a/clan home&e - РЎвЂљР ВµР В»Р ВµР С—Р С•РЎР‚РЎвЂљР С‘РЎР‚Р С•Р Р†Р В°РЎвЂљРЎРЉРЎРѓРЎРЏ Р Р…Р В° РЎвЂљР С•РЎвЂЎР С”РЎС“ Р С”Р В»Р В°Р Р…Р С•Р Р†Р С•Р С–Р С• Р Т‘Р С•Р СР В°"),
	HELP_TITLE("help.title", "&d&m===== &cMages&6Clans &d&m====="),
	HELP_FOOTER("help.footer", "&d&m===== &7by &eE.NeLsOn &d&m=====");
	
	private String path;
	private String message;
	
	Message(String path, String message) {
		this.path = path;
		this.message = message;
	}
	
	public String getMessage() {
		String result = SopClans.configLocale.getString(this.path);
		if(result != null) return result;
		return this.message;
	}
	
	public String getDefautMessage() {
		return this.message;
	}
	
	public String getMessageWithPlaceholders(Player player) {
		return ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, this.getMessage()));
	}
}

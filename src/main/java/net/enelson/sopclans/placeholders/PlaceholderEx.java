package net.enelson.sopclans.placeholders;

import java.util.List;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.enelson.sopclans.SopClans;
import net.enelson.sopclans.data.clan.Clan;
import net.enelson.sopclans.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public class PlaceholderEx extends PlaceholderExpansion {

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getAuthor() {
		return "E.NeLsOn";
	}

	@Override
	public String getIdentifier() {
		return "sopclans";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		// %sopclans_clan_<placeholder #>%
		//
		// %sopclans_top_<position>_<placeholder #>%
		//

		String[] st = identifier.split("_");

		String result = "sopclans";

		Placeholder pl = null;
		try {
			pl = Enum.valueOf(Placeholder.class, st[0].toUpperCase());
		} catch (IllegalArgumentException | NullPointerException e) {
			return "Invalid placeholder type. Must be 'top' or 'clan'";
		}
		Clan clan = null;
		List<String> pds;
		int i;
		switch (pl) {
			case CLAN:
				if (st.length != 2)
					return "Invalid parameters count";

				i = Integer.parseInt(st[1]);
				if (i == 0 && !st[1].equals("0")) {
					result = "Некорректный номер настройки(" + st[1] + ")";
					break;
				}

				clan = SopClans.cm.getClan(player);

				if (clan == null)
					pds = SopClans.configMain.getStringList("placeholders.clan_if_empty");
				else if (clan.getTag().equals(""))
					pds = SopClans.configMain.getStringList("placeholders.clan_if_tag_empty");
				else
					pds = SopClans.configMain.getStringList("placeholders.clan");

				if (pds != null && pds.size() > i) {
					result = pds.get(i);
				} else {
					result = "Invalid id or config";
					break;
				}

				if (clan != null) {
					result = result.replace("%rank%", clan.getMember(player).getRank().getDisplayName())
							.replace("%memberexp%", clan.getMember(player).getExp() + "");
				}

				break;
			case TOP:
				if (st.length != 3)
					return "Invalid parameters count";
				int position = Integer.parseInt(st[1]);
				i = Integer.parseInt(st[2]);
				clan = SopClans.cm.getTopClan(position);

				if (clan != null) {
					pds = SopClans.configMain.getStringList("placeholders.clan_top");
				} else
					pds = SopClans.configMain.getStringList("placeholders.clan_top_if_empty");

				if (pds != null && pds.size() > i) {
					result = pds.get(i);
				} else {
					result = "Invalid id or config";
					break;
				}

				break;
		}

		if (clan != null) {
			result = result.replace("%id%", clan.getId() + "").replace("%color%", Utils.getColor(clan.getLevel()))
					.replace("%name%", clan.getName()).replace("%level%", clan.getLevel() + "")
					.replace("%exp%", clan.getExp() + "").replace("%members%", clan.getMembersCount() + "")
					.replace("%slots%", clan.getSlotCount() + "")
					.replace("%freeslots%", (clan.getSlotCount() - clan.getMembersCount()) + "")
					.replace("%leader%", clan.getLeader().getPlayerName())
					.replace("%leaderuuid%", clan.getLeader().getUniqueId());

			if (clan.getTag().equals(""))
				result = result.replace("%tag%", "none");
			else
				result = result.replace("%tag%", clan.getTag());
		}

		return ChatColor.translateAlternateColorCodes('&', result);
	}
}

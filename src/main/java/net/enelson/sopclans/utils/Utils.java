package net.enelson.sopclans.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.enelson.sopclans.SopClans;

public class Utils {
	
	
	public static String getColor(int level) {
		String result = "";
		List<String> color = new ArrayList<String>();
		color.addAll(SopClans.configMain.getConfigurationSection("clan.level_colors").getKeys(false));
		color.sort(Comparator.comparing(c -> Integer.parseInt(c)));
		
		for(String l : color) {
			int lvl = Integer.parseInt(l);
			if(lvl<=level)
				result = SopClans.configMain.getString("clan.level_colors."+l);
			else
				break;
		}
		return result;
	}
	
	public static String getSerializedLocation(Location loc) {
		return loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
	}
	
	public static Location getDeserializedLocation(String s) {
		String[] split = s.split(",");
		return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]),
				Double.parseDouble(split[3]), Float.parseFloat(split[4]), Float.parseFloat(split[5]));
	}
}

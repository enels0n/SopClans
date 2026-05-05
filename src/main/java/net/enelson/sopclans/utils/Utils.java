package net.enelson.sopclans.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.ChatColor;

import net.enelson.sopclans.SopClans;

public class Utils {
	private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f0-9]{6})>");
	private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:#([A-Fa-f0-9]{6}):#([A-Fa-f0-9]{6})>(.*?)</gradient>");
	
	
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
		return color(result);
	}

	public static String color(String input) {
		if (input == null || input.isEmpty()) {
			return "";
		}

		String colored = applyGradients(input);
		colored = applyHex(colored);
		return ChatColor.translateAlternateColorCodes('&', colored);
	}

	private static String applyHex(String input) {
		Matcher matcher = HEX_PATTERN.matcher(input);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String replacement = net.md_5.bungee.api.ChatColor.of("#" + matcher.group(1)).toString();
			matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	private static String applyGradients(String input) {
		Matcher matcher = GRADIENT_PATTERN.matcher(input);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String replacement = buildGradient(matcher.group(1), matcher.group(2), matcher.group(3));
			matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	private static String buildGradient(String startHex, String endHex, String text) {
		if (text == null || text.isEmpty()) {
			return "";
		}
		int[] start = parseHex(startHex);
		int[] end = parseHex(endHex);
		int visibleChars = text.length();
		if (visibleChars <= 1) {
			return net.md_5.bungee.api.ChatColor.of("#" + startHex).toString() + text;
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			double ratio = (double) i / (double) (visibleChars - 1);
			int red = interpolate(start[0], end[0], ratio);
			int green = interpolate(start[1], end[1], ratio);
			int blue = interpolate(start[2], end[2], ratio);
			String hex = String.format("%02x%02x%02x", red, green, blue);
			builder.append(net.md_5.bungee.api.ChatColor.of("#" + hex)).append(text.charAt(i));
		}
		return builder.toString();
	}

	private static int[] parseHex(String hex) {
		return new int[] {
			Integer.parseInt(hex.substring(0, 2), 16),
			Integer.parseInt(hex.substring(2, 4), 16),
			Integer.parseInt(hex.substring(4, 6), 16)
		};
	}

	private static int interpolate(int start, int end, double ratio) {
		return (int) Math.round(start + (end - start) * ratio);
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

package net.enelson.sopclans;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;
import net.enelson.sopclans.commands.CommandManager;
import net.enelson.sopclans.data.clan.ClanManager;
import net.enelson.sopclans.data.clanwars.ClanWarsManager;
import net.enelson.sopclans.data.invite.InviteManager;
import net.enelson.sopclans.data.rank.RankManager;
import net.enelson.sopclans.database.SQLManager;
import net.enelson.sopclans.listeners.DamageHandler;
import net.enelson.sopclans.placeholders.PlaceholderEx;

public class SopClans extends JavaPlugin {

	public static FileConfiguration configMain;
	public static FileConfiguration configLocale;
	public static File fileConfig;
	public static File fileLocale;
	
	public static SQLManager sql;
	public static RankManager rm;
	public static ClanManager cm;
	public static ClanWarsManager cwm;
	public static InviteManager im;

	public static Economy economy;
	public static Boolean papi;
	public static Plugin plugin;
	
	public void onEnable() {
		SopClans.plugin = this;

		fileConfig = new File(getDataFolder(), "config.yml");
		if (!fileConfig.exists()) saveResource("config.yml", true);
		configMain = YamlConfiguration.loadConfiguration(fileConfig);
		
		fileLocale = new File(getDataFolder(), "locale.yml");
		if (!fileLocale.exists()) saveResource("locale.yml", true);
		configLocale = YamlConfiguration.loadConfiguration(fileLocale);

		economy = null;
		if(configMain.getBoolean("economy.enable")) {
			economy = (Economy)Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
			if(economy == null) {
				Bukkit.getServer().getPluginManager().disablePlugin(this);
				return;
			}
		}
		
		papi = false;
		if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PlaceholderEx().register();
			papi = true;
		}
		
		sql = new SQLManager(this);
		rm = new RankManager();
		cm = new ClanManager();
		cwm = new ClanWarsManager(this);
		im = new InviteManager();
		
		this.getCommand("clan").setExecutor(new CommandManager());
		Bukkit.getPluginManager().registerEvents(new DamageHandler(), this);
	}
	
	public void onDisable() {
		SopClans.sql.disconnect();
		SopClans.cwm.deInit();
	}
}

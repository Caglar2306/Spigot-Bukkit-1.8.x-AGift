package com.caglardev.agift;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.caglardev.agift.events.OnPlayerJoinEvent;
import com.caglardev.agift.objects.Gift;

public class Plugin extends JavaPlugin {
	
	// General
	public final String PLUGIN_NAME = "AGift";
	
	// Configuration files
	public static Configuration configurationDefault;
	public static String CONFIG_DEFAULT = "Config.yml";

	public static Configuration configurationTemplates;
	public static String CONFIG_TEMPLATES = "Template.yml";

	public static Configuration configurationGifts;
	public static String CONFIG_GIFTS = "Gifts.yml";

	public static Configuration configurationPlayers;
	public static String CONFIG_PLAYERS = "Players.yml";

	public static HashMap<Integer, Gift> gifts;
	public static HashMap<String, Calendar> players;
	
	@Override
	public void onDisable() {
		System.out.println("Saving players...");

		for(String key : configurationPlayers.getConfig().getConfigurationSection("Players").getKeys(false)) {
			configurationPlayers.getConfig().set("Players" + key, null);
		}
		
		for(Entry<String, Calendar> entry : players.entrySet()) {
			configurationPlayers.getConfig().set("Players." + entry.getKey(), entry.getValue().getTimeInMillis());
		}
		
		configurationPlayers.saveConfig();
		
		System.out.println("Players saved.");
	}

	@Override
	public void onEnable() {
		initializePlugin(this);
	}
	
	public void initializePlugin(JavaPlugin javaPlugin) {
		// Configuration files
		configurationDefault = new Configuration(javaPlugin, CONFIG_DEFAULT);
		configurationTemplates = new Configuration(javaPlugin, CONFIG_TEMPLATES);
		configurationGifts = new Configuration(javaPlugin, CONFIG_GIFTS);
		configurationPlayers = new Configuration(javaPlugin, CONFIG_PLAYERS);

		// Loading players
		if(!configurationPlayers.getConfig().contains("Players")) {
			configurationPlayers.getConfig().createSection("Players");
		}

		configurationPlayers.saveConfig();
		configurationPlayers.reloadConfig();
		
		players = new HashMap<String, Calendar>();
		
		for(String key : configurationPlayers.getConfig().getConfigurationSection("Players").getKeys(false)) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(configurationPlayers.getConfig().getLong("Players." + key, 0));
			players.put(key, calendar);
		}
		
		// Loading gifts
		if(!configurationGifts.getConfig().contains("Gifts")) {
			configurationGifts.getConfig().createSection("Gifts");
		}

		configurationGifts.saveConfig();
		configurationGifts.reloadConfig();
		
		gifts = new HashMap<Integer, Gift>();
		
		for(String key : configurationGifts.getConfig().getConfigurationSection("Gifts").getKeys(false)) {
			Material material = Material.getMaterial(configurationGifts.getConfig().getString("Gifts." + key + ".Material"));
			if(material != null) {
				Gift gift = new Gift(material, configurationGifts.getConfig().getInt("Gifts." + key + ".Stack", 1));
				
				if(configurationGifts.getConfig().getString("Gifts." + key + ".DisplayName") != null) {
					gift.setDisplayName(configurationGifts.getConfig().getString("Gifts." + key + ".DisplayName"));
				}
				
				gifts.put(gifts.size(), gift);
			}
		}

		this.getServer().getPluginManager().registerEvents(new OnPlayerJoinEvent(), this);
	}
	
}

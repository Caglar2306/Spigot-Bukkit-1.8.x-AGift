package com.caglardev.agift;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration {
    private FileConfiguration configuration = null;
    private File configurationFile = null;
	private JavaPlugin plugin;
	private String fileName;
	
	public Configuration(JavaPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		
		reloadConfig();
	}
	
    public void reloadConfig() {
        if(configurationFile == null) {
        	configurationFile = new File(plugin.getDataFolder(), fileName);
        }
        
        configuration = YamlConfiguration.loadConfiguration(configurationFile);
     
		try {
	        Reader defConfigStream;
			defConfigStream = new InputStreamReader(plugin.getResource(fileName), "UTF8");
			
	        if(defConfigStream != null) {
	            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	            configuration.setDefaults(defConfig);
	            getConfig().options().copyDefaults(true);
	            saveConfig();
	        }
		} catch(UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
    }
    
    public FileConfiguration getConfig() {
        if(configuration == null) {
            reloadConfig();
        }
        return configuration;
    }
    
    public void saveConfig() {
        if(configuration == null || configurationFile == null) {
            return;
        }
        
        try {
            getConfig().save(configurationFile);
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}

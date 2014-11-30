package de.fly4lol.updater.config;

import de.fly4lol.updater.Main;

public class Config {
	private Main plugin;
	
	public Config(Main plugin){
		this.plugin = plugin;
	}
	
	
	public void addDefaultConfig(){
		plugin.saveDefaultConfig();
	}
	
	public String getURL(){
		return plugin.getConfig().getString("url");
	}
	
	public String getUser(){
		return plugin.getConfig().getString("user");
	}
	
	public String getPassword(){
		return plugin.getConfig().getString("password");
	}
}

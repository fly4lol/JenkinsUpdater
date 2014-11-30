package de.fly4lol.updater;

import org.bukkit.plugin.java.JavaPlugin;

import de.fly4lol.updater.config.Config;

public class Main extends JavaPlugin{
	public Config config;
	
	
	@Override
	public void onEnable(){
		this.config = new Config(this);
	}
	
	public Config getUpdaterConfig(){
		return config;
	}

}

package de.fly4lol.updater;

import org.bukkit.plugin.java.JavaPlugin;

public class JenkinsUpdater extends JavaPlugin{
	private static JenkinsUpdater instance;
	
	@Override
	public void onEnable(){
		instance = this;
	}
	
	
	public static JenkinsUpdater getInstance(){
		return instance;
	}

	
	
}

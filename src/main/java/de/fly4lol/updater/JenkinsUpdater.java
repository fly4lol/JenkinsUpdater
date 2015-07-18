package de.fly4lol.updater;

import org.bukkit.plugin.java.JavaPlugin;

public class JenkinsUpdater extends JavaPlugin{
	private static JenkinsUpdater instance;
	private Repository repo;
	
	@Override
	public void onEnable(){
		this.repo = new Repository( this );
		instance = this;
	}
	
	
	public static JenkinsUpdater getInstance(){
		return instance;
	}
	
	public Repository getRepo(){
		return this.repo;
	}
	
	
}

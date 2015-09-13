package de.fly4lol.updater;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class JenkinsUpdater extends JavaPlugin{
	private static JenkinsUpdater instance;
	private Repository repo;
	public String prefix = "§7[§aUpdater§7] §r§7"; 
	
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
	
	public void sendMessage(Player player, String message){
		player.sendMessage( this.prefix + message);
	}
	
	
}

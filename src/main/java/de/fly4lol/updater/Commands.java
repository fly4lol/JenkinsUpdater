package de.fly4lol.updater;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.fly4lol.updater.util.Project;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;

public class Commands {
	private JenkinsUpdater plugin;
	
	public Commands(JenkinsUpdater plugin){
		this.plugin = plugin;
	}
	
	@Command(name = "test2")
	public void test(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		
		if( this.plugin.getRepo().getProjects() == null){
			Bukkit.broadcastMessage("Projects == null");
		}
		
		if(this.plugin.getRepo() == null){
			Bukkit.broadcastMessage("Repo == null");
		}
		List<Project> projects = this.plugin.getRepo().getUpdatebleProjects();
		for(Project project : projects){
			Bukkit.broadcastMessage( project.getJob().getName() );
		}
	}
	
	
	

}

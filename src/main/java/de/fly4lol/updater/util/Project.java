package de.fly4lol.updater.util;

import java.io.File;

import org.bukkit.plugin.Plugin;

import com.offbytwo.jenkins.model.Job;

public class Project {
	private Job job;
	private Plugin plugin;
	private File file;
	
	
	public Project(Job job , Plugin plugin){
		this.job = job;
		this.plugin = plugin;
		if(this.plugin == null){
			this.file = null;
		} else {
			this.file = this.getFileFromPlugin( plugin ); 
		}
		
	}
	
	
	public Project(String jobName, String pluginName){
		
	}
	
	
	
	public Job getJob(){
		return this.job;
	}
	
	public Plugin getPlugin(){
		return this.plugin;
	}
	
	public File getFile(){
		return this.file;
	}
	
	
	
	public static File getFileFromPlugin(Plugin plugin){
		return new File( plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
	}

}

package de.fly4lol.updater.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

import org.bukkit.plugin.Plugin;

import com.offbytwo.jenkins.model.Job;

import de.fly4lol.updater.JenkinsUpdater;

public class Project {
	private Job job;
	private Plugin plugin;
	private File file;
	private Long creationTime;
	
	
	public Project(Job job , Plugin plugin){
		this.job = job;
		this.plugin = plugin;
		if(this.plugin == null){
			this.file = null;
		} else {
			this.file = this.getFileFromPlugin( plugin );
			this.creationTime = this.getReadCreationTime( this.file );
		}
		
	}
	
	
	public Project(String jobName, String pluginName){
		this.job = JenkinsUpdater.getInstance().getRepo().getJobByName( jobName );
		this.plugin = JenkinsUpdater.getInstance().getServer().getPluginManager().getPlugin( pluginName);
		if(this.plugin == null){
			this.file = null;
		} else {
			this.file = this.getFileFromPlugin( this.plugin ); 
			this.creationTime = this.getReadCreationTime( this.file );
		}
	}
	
	public boolean isUpdateAvailable(){

		try {
			return this.getCreationTime() > this.job.details().getLastSuccessfulBuild().details().getTimestamp();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return false;
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
	
	public long getCreationTime(){
		return this.creationTime;
	}
	
	
	
	public static long getReadCreationTime(File file){
		try {
			return Files.readAttributes( file.toPath() , BasicFileAttributes.class ).lastModifiedTime().to( TimeUnit.MILLISECONDS );
		} catch (IOException e) {

			e.printStackTrace();
		}
		return 0;
	}
	
	public static File getFileFromPlugin(Plugin plugin){
		return new File( plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("%20", " "));
	}

}

package de.fly4lol.updater;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;

import de.fly4lol.updater.util.Project;
import de.pro_crafting.commandframework.CommandFramework;

public class Repository {
	private JenkinsUpdater plugin;
	private JenkinsServer server = null;
	private CommandFramework cmd;
	
	public Repository(JenkinsUpdater plugin){
		this.plugin = plugin;
		
		this.cmd = new CommandFramework( plugin );
		this.cmd.registerCommands( new Commands( plugin ));
		this.cmd.registerHelp();
	
	
	}
	
	public JenkinsServer getJenkinsServer(){
		if(this.server != null){
			return this.server;
		} 
		return new JenkinsServer( this.getURL(), this.getUser(), this.getPassword());
	}
	
	public List<Project> getProjects(){
		List<Project> projects = new ArrayList<Project>();
		List<String> projectNames = this.getProjectNames();
		if( projectNames == null ){
			Bukkit.broadcastMessage( "projectNames Null" );
		}
		for(String project : projectNames){
			if(project == null){
				Bukkit.broadcastMessage( "project null!");
			}
			
			if( this.getPluginNamebyProjectName( project ) == null){
				Bukkit.broadcastMessage(" plugin name  NULL");
			}
			
			if( projects ==  null ){
				Bukkit.broadcastMessage(" projects  null!");
			}
			projects.add( new Project( project , this.getPluginNamebyProjectName( project)) );
		}
		return projects;
	}
	
	public List<Project> getUpdatebleProjects(){
		List<Project> projects  = new ArrayList<Project>();
		List<Project> allProjects = this.getProjects();
		for(Project project : allProjects){
			if(project.isUpdateAvailable()){
				projects.add( project );
			}
		}
		return projects;
	}
	
	public Job getJobByName(String name){
		Map<String, Job> jobs = null;
		try {
			if(this.getJenkinsServer() == null){
				System.out.println("Server = null");
			}
			
			jobs = this.getJenkinsServer().getJobs();
			
			if(jobs == null){
				return null;
			}
			
			if(jobs.isEmpty()){
				return null;
			}
			
			if( name == null){
				return null;
			}
			
			List<Job> jobList = new ArrayList<Job>(jobs.values());
			for(Job job : jobList){

				if(job.getName().equalsIgnoreCase( name ) ){
					return job;
				}
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
		}		
	return null;
		
	}
	
	
	
	/*
	 * Config Stuff
	 */
	
	public void addDefaultConfig(){
		plugin.saveDefaultConfig();
	}
	
	public URI getURL(){
		String s = plugin.getConfig().getString( "url" );
		try {
			return new URI( s );
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getUser(){
		return plugin.getConfig().getString("user");
	}
	
	public String getPassword(){
		return plugin.getConfig().getString("password");
	}
	
	public String getPluginName(String jenkinsName){
		return  plugin.getConfig().getString("Projects." + jenkinsName  );
	}
	
	public  List<String> getProjectNames(){
		
		List<String> projects = new ArrayList<String>();
		projects.addAll( this.plugin.getConfig().getConfigurationSection( "Projects" ).getKeys( false ) );
		return projects;
	}
	
	public String getPluginNamebyProjectName(String projectName){
		return plugin.getConfig().getString("Projects." + projectName + ".Plugin");
	}

}

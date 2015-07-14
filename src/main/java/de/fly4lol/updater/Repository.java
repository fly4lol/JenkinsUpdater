package de.fly4lol.updater;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;

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

}

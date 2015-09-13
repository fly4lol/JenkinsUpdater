package de.fly4lol.updater;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
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
	
	public Build getBuildByNumber(Job job, int buildNumber){
		List<Build> builds;
		try {
			builds = job.details().getBuilds();
		} catch (IOException e) {
			return null;
		}
		
		for(Build build : builds){
			if(build.getNumber() ==  buildNumber){
				return build;
			}
		}
		return null;
	}
	
	public List<Project> getProjects(){
		List<Project> projects = new ArrayList<Project>();
		List<String> projectNames = this.getProjectNames();
		if(projectNames.isEmpty()){
			Bukkit.broadcastMessage( "projectNames Null" );
		}
		for(String project : projectNames){
			Bukkit.broadcastMessage( "12 PENIS!");
			projects.add( this.getProjectByName( project ));
		}
		return projects;
	}
	
	public Project getProjectByName(String project){
		if(project == null){
			Bukkit.broadcastMessage("Project == null");
			return null;
		}
		
		if(!this.existsProject( project )){
			return null;
		}
		
		if( this.getPluginNamebyProjectName( project ) == null){
			return null;
		}
		
		return new Project( project , this.getPluginNamebyProjectName( project)) ;
	}
	
	public boolean existsProject(String project){
		return this.getProjectNames().contains( project);
	}
	
	public List<Project> getUpdatebleProjects(){
		List<Project> projects  = new ArrayList<Project>();
		List<Project> allProjects = this.getProjects();
		if(allProjects.isEmpty()){
			Bukkit.broadcastMessage("No projects");
		}
		for(Project project : allProjects){
			if(project.isUpdateAvailable()){
				projects.add( project );
			}
		}
		if(projects.isEmpty()){
			Bukkit.broadcastMessage("No Updateple Projects");
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
		Bukkit.broadcastMessage( "14");
		
		List<String> projects = new ArrayList<String>();
		Bukkit.broadcastMessage( "15");
		projects.addAll( this.plugin.getConfig().getConfigurationSection( "Projects" ).getKeys( false ) );
		Bukkit.broadcastMessage( "16");
		return projects;
	}
	
	public String getPluginNamebyProjectName(String projectName){
		return plugin.getConfig().getString("Projects." + projectName + ".Plugin");
	}
	
	public void download(InputStream in , File file){
		
		OutputStream outputStream = null;
		
		try {
			outputStream =  new FileOutputStream( file );
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 
			}
		}
	
	}

}

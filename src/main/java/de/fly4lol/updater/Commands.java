package de.fly4lol.updater;

import java.io.IOException;
import java.util.List;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.offbytwo.jenkins.model.Artifact;
import com.offbytwo.jenkins.model.Build;

import de.fly4lol.messenger.MessageHolder;
import de.fly4lol.messenger.Messenger;
import de.fly4lol.messenger.page.PageHolder;
import de.fly4lol.messenger.page.PageManager;
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
	
	@Command(name = "updater" , permission = "updater.use")
	public void updater(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		MessageHolder message = new MessageHolder();
		message.addLine(plugin.prefix + "Command nicht gefunden");
		if(player.hasPermission("updater.check")){
			message.addLine( new FancyMessage(plugin.prefix + "Nutze: /updater check").command("/updater check").tooltip("§4Klicke Hier!"));
		}
		if(player.hasPermission("updater.add")){
			message.addLine( new FancyMessage(plugin.prefix + "Nutze: /updater add").command("/updater add").tooltip("§4Klicke Hier!"));
		}
		if(player.hasPermission("updater.updateinfo")){
			message.addLine( new FancyMessage(plugin.prefix + "Nutze: /updater updateinfo <project>").suggest("/updater updateinfo <project>").tooltip("§4Klicke Hier!"));
		}
		if(player.hasPermission("updater.builds")){
			message.addLine( new FancyMessage(plugin.prefix + "Nutze: /updater builds <project>").suggest("/updater builds <project>").tooltip("§4Klicke Hier!"));
		}
		
		if(player.hasPermission("updater.artifacts")){
			message.addLine( new FancyMessage(plugin.prefix + "Nutze: /updater artifacts <project> <build>").suggest("/updater builds <project> <build>").tooltip("§4Klicke Hier!"));
		}
		Messenger.getInstance().send( player , message);
		
	}
	
	@Command(name = "updater.check" , permission = "updater.check")
	public void updaterCheck(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		Bukkit.broadcastMessage( "1");
		List<Project> projects = this.plugin.getRepo().getUpdatebleProjects();
		Bukkit.broadcastMessage( "2");
		
		if(projects.isEmpty()){
			plugin.sendMessage( player, "Alle Plugins sind UpToDate!");
			plugin.sendMessage(player, "LULULU!!ELF!");
			return;
		}
		
		MessageHolder message = new MessageHolder();
		if(projects.size() == 1){
			message.addLine( plugin.prefix + "Es ist für ein Plugin §6ein§7 Update verfügpar!");
		} else {
			message.addLine( plugin.prefix + "Es sind für §6" + projects.size() + " §7Plugins Updates verfügpar!");
		}
		for(Project project : projects){
			FancyMessage fMessage = new FancyMessage( plugin.prefix + project.getJob().getName()).command("/updater updateinfo " + project.getJob().getName() ).tooltip("§4Klicke");
			message.addLine( fMessage );
		}
		
		Messenger.getInstance().send( player , message);
		
		
	}
	
	
	@Command(name = "updater.updateinfo" , permission = "updater.updateinfo")
	public void updaterUpdateinfo(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		if(args.length != 1){
			plugin.sendMessage( player  , "Nutze: /updater updateinfo <project>");
			return;
		}
		
		String name = args[0];
		
		if(!plugin.getRepo().existsProject( name )){
			plugin.sendMessage( player , "Es Existier kein Project mit diesem Namen!");
			return;
		}
		
		Project project = plugin.getRepo().getProjectByName( name );
		
		if(!project.isUpdateAvailable()){
			MessageHolder message = new MessageHolder();
			message.addLine( plugin.prefix + "Es ist im momment kein Update verfügbar!");
			message.addLine( new FancyMessage("                   §7§oZu einem Build downgraden").tooltip("§5Klicke Hier!").command("/Updater builds " + name));
			Messenger.getInstance().send( player, message);
		}
		
		MessageHolder message = new MessageHolder();
		message.addLine( plugin.prefix + "Es steht ein Neuerer Build zur Verfüguging!");
		try {
			int build = project.getJob().details().getLastSuccessfulBuild().getNumber();
			message.addLine( new FancyMessage("                   §7§oKlicke hier um zu Build " + build + " zu Updatern ")
			.tooltip("§5Klicke Hier!").command("/Updater artifacts " + name + " " + build));
		} catch (IOException e) {
			message.addLine(plugin.prefix + "Es iste in fehler aufgetreten bitte schaue in die Konsole! Oder melde es einem Administrator!");
			e.printStackTrace();
		}
		Messenger.getInstance().send( player, message);
		
	}
	
	@Command(name = "updater.builds" , permission = "updater.builds")
	public void updaterBuilds(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
	}
	
	@Command(name = "updater.artifacts" , permission = "updater.artifacts")
	public void updaterArtofacts(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		
		if(args.length != 2){
			plugin.sendMessage( player , "Nutze: /updater artifacts <project> <build>");
			return;
		}
		
		String name = args[0];
		int number;
		try {
			number = Integer.parseInt( args[1]);
		} catch (Exception e) {
			plugin.sendMessage( player , "Der Build muss eine Zahl sein!");
			return;
		}
		
		if(!plugin.getRepo().existsProject( name )){
			plugin.sendMessage( player , "Es Existiert kein Project mit diesem Namen!");
			return;
		}
		
		Project project = plugin.getRepo().getProjectByName( name );
		Build build = plugin.getRepo().getBuildByNumber( project.getJob(), number);
		
		if(build == null ){
			plugin.sendMessage( player , "Es Existiert kein Build mit dieser nummer!");
			return;
		}
		
		List<Artifact> artifacts = null;
		try {
			artifacts = build.details().getArtifacts();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(artifacts ==  null){
			plugin.sendMessage(player, "Es gibt keine Artifacte!");
			return;
		}
		PageHolder page = new PageHolder();
		page.setPageHeader("Wähle ein Artifact!");
		int i = 0;
		for(Artifact artifact : artifacts){
			page.addLine( new FancyMessage("§a" + i + " §7§o" + artifact.getDisplayPath()).tooltip("§4Klicke Hier!").command("/updater update " + name + " " + i) );
		}
		PageManager.getInstance().send( player , page );
	}
	
	@Command(name = "updater.add" , permission = "updater.add")
	public void updaterAdd(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
	}
		
		
	
	
	

}

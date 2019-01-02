package me.Xeroun.McMExtras;

import java.util.HashMap;

import me.Xeroun.McMExtras.ExpBar.ExpBarCommands;
import me.Xeroun.McMExtras.ExpBar.ExpBarEvents;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class McMExtras extends JavaPlugin {

	private static McMExtras instance;
	public static McMExtras getInstance(){
		return instance;
	}
	
	private HashMap<String, PlayerData> data = new HashMap<String, PlayerData>();
	public PlayerData getData(String player){
		if(!data.containsKey(player)){
			PlayerData newData = new PlayerData(player);
			data.put(player, newData);
		}
		return data.get(player);
	}
	
	public void clearData(String player){
		if(data.containsKey(player)){
			data.remove(player);
		}
	}
	
	
	public void onEnable() {
		if(getServer().getPluginManager().getPlugin("mcMMO") != null){
			getLogger().info(this + " has been enabled.");
			getConfig().options().copyDefaults(true);
			saveConfig();
			
			McMExtras.instance = this;
			
			getServer().getPluginManager().registerEvents(new ExpBarEvents(), this);
			getCommand("expbar").setExecutor(new ExpBarCommands());
		}else{
			getLogger().info(this + " requires mcMMO to function.");
		}	
	}

	public void onDisable() {
		getLogger().info(this + " has been disabled.");

	}

	public String getPrefix() {
		return ChatColor.YELLOW + "[" + ChatColor.GOLD + this.getName() + ChatColor.YELLOW + "] " + ChatColor.RESET;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (label.equalsIgnoreCase("mcme")) {
			if (args.length >= 1) {

				if (args[0].equalsIgnoreCase("v")) {

					sender.sendMessage(ChatColor.AQUA + "Name: " + ChatColor.GREEN + getName());
					sender.sendMessage(ChatColor.AQUA + "Version: " + ChatColor.GREEN + this.getDescription().getVersion());
					sender.sendMessage(ChatColor.AQUA + "Author: " + ChatColor.GREEN + this.getDescription().getAuthors());

				} else {
					sender.sendMessage(this.getPrefix() + ChatColor.RED + "Invalid arguments.");
				}

			} else {
				sender.sendMessage(this.getPrefix() + ChatColor.AQUA + "Commands: " + ChatColor.GREEN + "/mcme v");
			}
		}

		return true;
	}

}
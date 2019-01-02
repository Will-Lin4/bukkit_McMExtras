package me.Xeroun.McMExtras.ExpBar;

import me.Xeroun.McMExtras.McMExtras;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExpBarCommands implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (label.equalsIgnoreCase("expbar")) {
			if (args.length >= 1) {
				
				if (args[0].equalsIgnoreCase("toggle")) {

					if(sender instanceof Player){
						Player player = (Player) sender;
						
						if(McMExtras.getInstance().getData(player.getName()).enabled()){
							player.sendMessage(McMExtras.getInstance().getPrefix() + ChatColor.AQUA + "The exp bar has been disabled.");
							FakeDragon.setBossBar(player, "", 100);
							McMExtras.getInstance().getData(player.getName()).setEnabled(false);
							McMExtras.getInstance().getData(player.getName()).setSkill("");
						}else{
							player.sendMessage(McMExtras.getInstance().getPrefix() + ChatColor.AQUA + "The exp bar has been enabled.");
							McMExtras.getInstance().getData(player.getName()).setEnabled(true);
						}
						
					}
					
				} else {
					sender.sendMessage(McMExtras.getInstance().getPrefix() + ChatColor.RED + "Invalid arguments.");
				}

			} else {
				sender.sendMessage(McMExtras.getInstance().getPrefix() + ChatColor.AQUA + "Commands: " + ChatColor.GREEN + "/expbar toggle");
			}
		}

		return true;
	}
	
}

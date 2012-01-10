package com.wwsean08.CreativeWarning;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CreativeWarning extends JavaPlugin{
	FileConfiguration config;
	List<Integer> bannedItems;
	SpoutCreativeWarningInventoryListener invListener;
	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		invListener = new SpoutCreativeWarningInventoryListener(this);
		loadConfig();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Type.CUSTOM_EVENT, invListener, Priority.Monitor, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission("InvWarning.admin"))
				loadConfig();
		}else if(sender instanceof ConsoleCommandSender){
			loadConfig();
		}
		return true;
	}

	private void loadConfig(){
		config = this.getConfig();
		File ConfigFile = new File(this.getDataFolder() + "config.yml");
		if(!ConfigFile.exists()){
			config.options().copyDefaults(true);
			this.saveConfig();
		}
		bannedItems = config.getIntegerList("bannedItems");
	}
}

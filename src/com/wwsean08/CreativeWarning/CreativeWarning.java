package com.wwsean08.CreativeWarning;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CreativeWarning extends JavaPlugin{
	List<Integer> bannedItems;
	SpoutCreativeWarningInventoryListener invListener;
	boolean blockMode;
	@Override
	public void onDisable() {

	}

	@Override
	public void onEnable() {
		invListener = new SpoutCreativeWarningInventoryListener(this);
		loadConfig();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvent(Type.CUSTOM_EVENT, invListener, Priority.Low, this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender instanceof Player){
			Player player = (Player)sender;
			if(player.hasPermission("CreativeWarning.admin")){
				this.reloadConfig();
				bannedItems = getConfig().getIntegerList("bannedItems");
				blockMode = getConfig().getBoolean("blockMode");
				player.sendMessage(ChatColor.GRAY + "Creative Warning Config Reloaded");
			}
		}else if(sender instanceof ConsoleCommandSender){
			this.reloadConfig();
			bannedItems = getConfig().getIntegerList("bannedItems");
			blockMode = getConfig().getBoolean("blockMode");
			sender.sendMessage("Creative Warning Config Reloaded");
		}
		return true;
	}

	private void loadConfig(){
		if(!new File(this.getDataFolder().getPath() + "/config.yml").exists())
			getConfig().options().copyDefaults(true);
		bannedItems = getConfig().getIntegerList("bannedItems");
		blockMode = getConfig().getBoolean("blockMode");
		saveConfig();
	}
}

package com.wwsean08.CreativeWarning;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.event.inventory.InventoryCloseEvent;
import org.getspout.spoutapi.event.inventory.InventoryListener;
import org.getspout.spoutapi.event.inventory.InventoryOpenEvent;

public class SpoutCreativeWarningInventoryListener extends InventoryListener{
	HashMap<Player, ItemStack[]> inv;
	CreativeWarning plugin;
	Logger log = Bukkit.getServer().getLogger();
	public SpoutCreativeWarningInventoryListener(CreativeWarning instance){
		plugin = instance;
		inv = new HashMap<Player, ItemStack[]>();
	}
	@Override
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = event.getPlayer();
		if(player.getGameMode().equals(GameMode.CREATIVE)){
			if(!player.hasPermission("CreativeWarning.ignore")){
				inv.put(player, player.getInventory().getContents());
			}
		}
	}

	@Override
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = event.getPlayer();
		if(!player.hasPermission("CreativeWarning.ignore")){
			if(player.getGameMode().equals(GameMode.CREATIVE)){
				List<Integer> bannedList = plugin.bannedItems;
				ItemStack[] contents = player.getInventory().getContents();
				ItemStack[] oldContents = inv.get(player);
				if(contents != null && oldContents != null){
					for(ItemStack IS : contents){
						if(IS != null){
							for(Integer i : bannedList){
								if(IS.getTypeId() == i){
									boolean found = false;
									for(ItemStack oldIS : oldContents){
										if(oldIS != null){
											if(oldIS.getTypeId() == i){
												found = true;
												break;
											}
										}
									}
									if(!found){	//if it was found that means they had it before and we already would have warned the admins
										warnAdmins(player, i);
									}
								}
							}
						}
					}
				}
				inv.remove(player);
				if(plugin.blockMode){
					for(Integer i : plugin.bannedItems){
						player.getInventory().remove(i);
					}
				}
			}
		}
	}
	private void warnAdmins(Player player, int i) {
		Player[] online = Bukkit.getServer().getOnlinePlayers();
		ItemStack IS = new ItemStack(i);
		for(Player p : online){
			if(p.hasPermission("CreativeWarning.admin")){
				p.sendMessage(ChatColor.GRAY + player.getName() + " tried to add " + IS.getType().toString().toLowerCase().replace("_", " ") + " to their inventory");
			}
		}
		log.warning(ChatColor.GRAY + player.getName() + " tried to add " + IS.getType().toString().toLowerCase().replace("_", " ") + " to their inventory");
	}
}
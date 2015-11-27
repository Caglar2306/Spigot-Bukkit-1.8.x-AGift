package com.caglardev.agift.events;

import java.util.Calendar;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.caglardev.agift.Plugin;
import com.caglardev.agift.objects.Gift;

public class OnPlayerJoinEvent implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		if(Plugin.players.containsKey(player.getName())) {
			Calendar currentCalendar = Calendar.getInstance();
			Calendar playerCalendar = Calendar.getInstance();
			playerCalendar.setTime(Plugin.players.get(player.getName()).getTime());
			
			long difference = currentCalendar.getTimeInMillis() - playerCalendar.getTimeInMillis();
			int days = Plugin.configurationDefault.getConfig().getInt("Days", 1);
			
			if(difference >= 1000 * 60 * 60 * 24 * days) {
				givePlayerGift(player);
			}
		} else {
			Plugin.players.put(player.getName(), Calendar.getInstance());
		}
	}
	
	private void givePlayerGift(Player player) {
		Random random = new Random();
		int randomId = random.nextInt(Plugin.gifts.size());
		Gift randomGift = Plugin.gifts.get(randomId);

		if(canPlayerPickupItems(player)) {
			if(randomGift.getMaterial() != null) {
				ItemStack randomItem = new ItemStack(randomGift.getMaterial(), randomGift.getStack());
				ItemMeta itemMeta = randomItem.getItemMeta();
				
				if(randomGift.getDisplayName() != null) {
					itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', randomGift.getDisplayName()));
				}
				
				randomItem.setItemMeta(itemMeta);
				
				player.getInventory().addItem(randomItem);
				
				String displayName = "";
				
				if(randomGift.getDisplayName() != null) {
					displayName = ChatColor.translateAlternateColorCodes('&', randomGift.getDisplayName());
				}
				
				String message = ChatColor.translateAlternateColorCodes('&', Plugin.configurationTemplates.getConfig().getString("YOU_GOT_A_ITEM"))
					.replaceAll("%itemStack%", String.valueOf(randomGift.getStack()))
					.replaceAll("%itemName%", randomGift.getMaterial().toString())
					.replaceAll("%displayName%", displayName);
				
				player.sendMessage(message);
				Plugin.players.remove(player.getName());
				Plugin.players.put(player.getName(), Calendar.getInstance());
			}
		} else {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', Plugin.configurationTemplates.getConfig().getString("YOU_CANT_PICKUP")));
		}
	}

	public boolean canPlayerPickupItems(Player player) {
		for(ItemStack itemStack : player.getInventory()) {
			if(itemStack == null) {
				return true;
			}
		}
		
		return false;
	}
	
}

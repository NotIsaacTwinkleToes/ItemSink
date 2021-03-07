package me.isaac.itemsink.events;

import org.apache.commons.lang.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import me.isaac.itemsink.ItemSink;
import me.isaac.itemsink.util.MultiInventory;

public class InventoryChecking implements Listener {
	
	ItemSink plugin;
	public InventoryChecking(ItemSink pl) {
		plugin = pl;
	}

	@EventHandler
	public void onInvClick(InventoryClickEvent e) {

		MultiInventory mi;
		
		Player player = (Player) e.getWhoClicked();

		try {
			mi = MultiInventory.getMultiInventoryFromInventory(e.getInventory());
		} catch (NullPointerException e1) {
			return;
		}
		
		if (mi.containsInventory(e.getInventory()))
			e.setCancelled(true);

		int slot = e.getRawSlot();
		
		try {
			switch (slot) {
			case 45:
				mi.previousPage(player);
				break;
			case 49:
				mi.setWhitelist(!mi.getWhitelist());
				break;
			case 53:
				mi.nextPage(player);
				break;
			default:
				break;
			}
		} catch (Exception e1) {
		}
		
		ItemStack item = e.getCurrentItem();
		
		if (item == null)
			return;
		
		if (slot >= 0 && slot < 45) {
			mi.removeItem(item);
			
			PlayBar bar = new PlayBar(WordUtils.capitalize(item.getType().toString()), mi, plugin);
			bar.setProgress(1);
			bar.add(player);
			bar.remove(1,  .01);
			
			
		} else if (slot > 53 && slot < 90) {
			
			if (mi.addItem(item) == null)
				return;
			PlayBar bar = new PlayBar(WordUtils.capitalize(item.getType().toString()), mi, plugin);
			bar.setProgress(0);
			bar.add(player);
			bar.add(1, 0.01);
			
		}
		
	}

}

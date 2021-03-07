package me.isaac.itemsink.util;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MultiInventory {

	public static ArrayList<MultiInventory> invs = new ArrayList<MultiInventory>();

	boolean whitelist;
	ArrayList<ItemStack> items;
	ArrayList<Player> viewers;
	ArrayList<Inventory> inventories;
	HashMap<Player, Integer> currentPage;

	public MultiInventory() {

		whitelist = true;

		viewers = new ArrayList<Player>();
		items = new ArrayList<ItemStack>();
		inventories = new ArrayList<Inventory>();
		currentPage = new HashMap<Player, Integer>();

		createInventory();

		invs.add(this);

	}

	private Inventory createInventory() {
		Inventory inv = Bukkit.createInventory(null, 9 * 6, " ");
		inventories.add(inv);
		setup();
		return inv;
	}

	public ItemStack addItem(ItemStack item) {

		if (item == null)
			return null;

		for (ItemStack items : items) {
			if (items.getType().equals(item.getType()))
				return null;
		}

		items.add(new ItemStack(item.getType()));
		setup();
		updateDisplay();
		return item;
	}

	public void removeItem(@Nonnull ItemStack item) {
		if (item == null)
			return;
		if (items.contains(item))
			items.remove(item);
		updateDisplay();
	}

	private void setup() {

		ItemStack back = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta backm = back.getItemMeta();
		backm.setDisplayName(ChatColor.RED + "Previous Page");
		back.setItemMeta(backm);

		ItemStack next = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
		ItemMeta nextm = next.getItemMeta();
		nextm.setDisplayName(ChatColor.GREEN + "Next Page");
		next.setItemMeta(nextm);

		ItemStack white = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta whitem = white.getItemMeta();
		ArrayList<String> whiteLore = new ArrayList<String>();
		whiteLore.add(ChatColor.WHITE + "These items will float in water.");
		whiteLore.add(ChatColor.WHITE + "Click me to switch to a blacklist.");
		whitem.setLore(whiteLore);
		whitem.setDisplayName(ChatColor.WHITE + "Whitelist");
		white.setItemMeta(whitem);

		ItemStack black = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta blackm = black.getItemMeta();
		blackm.setDisplayName(ChatColor.DARK_GRAY + "Blacklist");
		ArrayList<String> blackLore = new ArrayList<String>();
		blackLore.add(ChatColor.DARK_GRAY + "These items will not float in water.");
		blackLore.add(ChatColor.DARK_GRAY + "Click me to switch to a whitelist.");
		blackm.setLore(blackLore);
		black.setItemMeta(blackm);
		
		ItemStack info = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
		ItemMeta infom = info.getItemMeta();
		infom.setDisplayName(ChatColor.YELLOW + "Information");
		ArrayList<String> infoLore = new ArrayList<String>();
		infoLore.add(ChatColor.YELLOW + "Click items in your inventory to add to the " + (whitelist ? "whitelist" : "blacklist") + ".");
		infoLore.add(ChatColor.YELLOW + "Click items in this inventory to remove from the " + (whitelist ? "whitelist" : "blacklist") + ".");
		infom.setLore(infoLore);
		info.setItemMeta(infom);

		for (Inventory inv : inventories) {
			inv.setItem(45, back);
			inv.setItem(48, info);
			inv.setItem(49, (whitelist ? white : black));
			inv.setItem(50, info);
			inv.setItem(53, next);
		}

	}

	public boolean getWhitelist() {
		return whitelist;
	}

	public void setWhitelist(boolean whitelist) {
		this.whitelist = whitelist;
		updateDisplay();
	}

	public boolean whitelistContains(ItemStack item) {
		for (ItemStack items : items) {
			if (items.getType().equals(item.getType()))
				return true;
		}
		return false;
	}
	
	public ArrayList<ItemStack> whitelistItems() {
		return items;
	}

	public ArrayList<Player> getViewers() {
		return viewers;
	}

	public void nextPage(Player player) {
		
		Inventory inv = inventories.get(currentPage.get(player) + 1);
		
		currentPage.put(player, currentPage.get(player) + 1);
		player.openInventory(inv);

	}

	public void previousPage(Player player) {
		
		Inventory inv = inventories.get(currentPage.get(player) - 1);
		
		currentPage.put(player, currentPage.get(player) - 1);
		player.openInventory(inv);
	}

	public void open(Player player) {

		player.openInventory(inventories.get(0));
		currentPage.put(player, 0);

	}

	public boolean containsInventory(Inventory inventory) {

		for (Inventory inventories : this.inventories) {
			if (inventories.equals(inventory))
				return true;
		}

		return false;

	}

	public static MultiInventory getMultiInventoryFromInventory(Inventory inventory) {

		for (MultiInventory mi : MultiInventory.invs) {
			if (mi.containsInventory(inventory))
				return mi;
		}

		return null;
	}

	private void updateDisplay() {

		for (Inventory invs : inventories) {
			invs.clear();
			setup();
		}

		Inventory inv = inventories.get(0);

		int counter = 0;

		for (ItemStack items : items) {

			if (inv.firstEmpty() >= 46) {
				counter++;

				try {
					inv = inventories.get(counter);
				} catch (IndexOutOfBoundsException e) {
					inv = createInventory();
				}
			}

			inv.addItem(items);

		}

	}

}

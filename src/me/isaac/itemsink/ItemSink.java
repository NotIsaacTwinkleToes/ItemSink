package me.isaac.itemsink;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.isaac.itemsink.commands.ItemSinkGUI;
import me.isaac.itemsink.events.InventoryChecking;
import me.isaac.itemsink.util.MultiInventory;

public class ItemSink extends JavaPlugin {
	
	private static MultiInventory whitelist;
	
	public void onEnable() {
		
		registerCommands();
		registerEvents();
		
		whitelist = new MultiInventory();
		
		new BukkitRunnable() {
			public void run() {
				for (World worlds : Bukkit.getWorlds()) {
					for (Entity entities : worlds.getEntities()) {
						if (entities instanceof Item) {
							
							if (whitelist.getWhitelist()) {
								if (whitelist.whitelistContains(((Item) entities).getItemStack()))
									continue;
								
								if (entities.isInWater()) {
									
									Vector velo = entities.getVelocity();
									
									velo.setY(velo.getY() - .01);
									
									if (velo.getY() < -.05) {
										velo.setY(-.04);
									}
									
									entities.setVelocity(velo);
									
								}
							} else {
								if (!whitelist.whitelistContains(((Item) entities).getItemStack()))
									continue;
								
								if (entities.isInWater()) {
									
									Vector velo = entities.getVelocity();
									
									velo.setY(velo.getY() - .01);
									
									if (velo.getY() < -.05) {
										velo.setY(-.04);
									}
									
									entities.setVelocity(velo);
									
								}
							}
							
							
						}
					}
				}
				
			}
		}.runTaskTimer(this, 1, 5);
		
		for (String material : yaml.getStringList("Whitelist")) {
			
			whitelist.addItem(new ItemStack(Material.valueOf(material)));
			
		}
		
		whitelist.setWhitelist(yaml.getBoolean("IsWhitelist"));
		
		file.delete();
		
	}
	
	File file = new File("plugins//ItemSink//whitelist.yml");
	YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
	
	public void onDisable() {
		
		try {
			file.createNewFile();
		} catch (IOException e) {
		}
		
		ArrayList<String> materials = new ArrayList<String>();
		
		for (ItemStack items : whitelist.whitelistItems()) {
			materials.add(items.getType().toString());
		}
		
		yaml.set("Whitelist", materials);
		yaml.set("IsWhitelist", whitelist.getWhitelist());
		
		try {
			yaml.save(file);
		} catch (IOException e) {
		}
		
	}
	
	public void registerCommands() {
		
		getCommand("itemsinkgui").setExecutor(new ItemSinkGUI());
		
	}
	
	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new InventoryChecking(this), this);
		
	}
	
	public static MultiInventory getWhitelist() {
		return whitelist;
	}
	
}

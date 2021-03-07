package me.isaac.itemsink.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.isaac.itemsink.ItemSink;
import me.isaac.itemsink.util.MultiInventory;

public class PlayBar {
	
	BossBar bar;
	ItemSink plugin;
	
	
	public PlayBar(String title, MultiInventory mi, ItemSink plugin) {
		
		BarColor color = (mi.getWhitelist() ? BarColor.BLUE : BarColor.RED);
		title = (mi.getWhitelist() ? title : ChatColor.DARK_GRAY + title);
		
		bar = Bukkit.createBossBar(title, color, BarStyle.SOLID);
		bar.setProgress(0);
		
		this.plugin = plugin;
		
	}
	
	public void setProgress(double progress) {
		bar.setProgress(progress);
	}
	
	public void add(Player player) {
		bar.addPlayer(player);
	}
	
	public void add(int time, double doub) {
		
		new BukkitRunnable() {
			public void run() {
				
				double progress = bar.getProgress() + doub;
				
				if (progress >= 1) {
					bar.removeAll();
					cancel();
				} else {
					bar.setProgress(progress);
				}
				
				
				
			}
		}.runTaskTimer(plugin, 0, time);
		
	}
	
	public void remove(int time, double doub) {
		
		new BukkitRunnable() {
			public void run() {
				double progress = bar.getProgress() - doub;
				
				if (progress <= 0) {
					bar.removeAll();
					cancel();
				} else {
					bar.setProgress(progress);
				}
				
				
				
			}
		}.runTaskTimer(plugin, 0, time);
		
	}

}

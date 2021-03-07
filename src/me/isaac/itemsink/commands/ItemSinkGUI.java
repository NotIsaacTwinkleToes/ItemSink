package me.isaac.itemsink.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.isaac.itemsink.ItemSink;
import me.isaac.itemsink.util.MultiInventory;

public class ItemSinkGUI implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player))
			return true;
		
		Player player = (Player) sender;
		
		MultiInventory inv = ItemSink.getWhitelist();
		
		inv.open(player);
		
		return true;
	}

}

package ru.kronos.bluelib.api.template;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public abstract class BlueLibCommand implements CommandExecutor, TabCompleter {
	
	protected BlueLibCommand(JavaPlugin plugin, String commandName) {
		plugin.getCommand(commandName).setExecutor(this);
		plugin.getCommand(commandName).setTabCompleter(this);
	}
	
	protected BlueLibCommand(BlueLibPlugin plugin, String commandName) {
		this(plugin.getPlugin(), commandName);
	}
	
	@Override
	public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
	
	@Override
	public abstract List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args);
	
}

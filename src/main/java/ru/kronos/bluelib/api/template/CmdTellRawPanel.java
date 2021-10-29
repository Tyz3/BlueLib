package ru.kronos.bluelib.api.template;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import ru.kronos.bluelib.Main;

import java.util.List;

public abstract class CmdTellRawPanel extends ChatControlPanel implements CommandExecutor, TabCompleter {
	
	protected CmdTellRawPanel(String name) {
		Main.inst.getCommand(name).setExecutor(this);
		Main.inst.getCommand(name).setTabCompleter(this);
	}
	
	@Override
	public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
	
	@Override
	public abstract List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args);
	
}

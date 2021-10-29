package ru.kronos.combatlog.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.combatlog.Main;
import ru.kronos.combatlog.CombatLogEngine;

public class CombatCMD extends BlueLibCommand {

	private static CombatCMD combatCMD;

	private CombatCMD() {
		super(Main.inst, "combat");
	}

	public static void newInstance() {
		if (combatCMD == null) {
			combatCMD = new CombatCMD();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload") && sender.hasPermission("combatlog.admin")) {
				Main.inst.disable();
				Main.inst.enable();
				sender.sendMessage("§aCombatLog has been reloaded.");
				return true;
			}
		}
		
		if (sender.equals(Bukkit.getConsoleSender())) return false;
		
		CombatLogEngine.checkCombat((Player) sender);
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		return null;
	}

}

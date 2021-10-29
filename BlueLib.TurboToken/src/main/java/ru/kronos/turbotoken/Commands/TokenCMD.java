package ru.kronos.turbotoken.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.util.CompleteHelper;
import ru.kronos.turbotoken.Main;
import ru.kronos.turbotoken.TokenEngine;

import java.util.List;

public class TokenCMD extends BlueLibCommand {
	
	public TokenCMD() {
		super(Main.inst, "token");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission("turbotokens.admin")) return false;
		
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
				Main.inst.disable();
				Main.inst.enable();
				sender.sendMessage("TurboTokens settings reloaded.");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("update") || args[0].equalsIgnoreCase("u")) {
				if (sender.getName().equals("CONSOLE")) return false;
				Player p = (Player) sender;
				TokenEngine.updateItemInHand(p);
				sender.sendMessage("Предмет у вас в руках обновлён.");
				return true;
			}
			
			if (args[0].equalsIgnoreCase("hand") || args[0].equalsIgnoreCase("h")) {
				if (sender.getName().equals("CONSOLE")) return false;
				Player p = (Player) sender;
				TokenEngine.sendEnchants(p);
				return true;
			}
			
//			if (args[0].equalsIgnoreCase("analyze") || args[0].equalsIgnoreCase("a")) {
//				if (sender.getName().equals("CONSOLE")) return false;
//				Player p = (Player) sender;
//				TokenEngine.analyze(p);
//				return true;
//			}
			
			
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		if (!sender.hasPermission("turbotokens.admin")) return null;
		
		if (args.length == 1) {
			return CompleteHelper.filter(args, "update", "hand", "reload");
		}
		
		return null;
	}

}

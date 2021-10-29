package ru.kronos.bluelib.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.engine.PluginEngine;
import ru.kronos.bluelib.api.engine.StorageEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.api.util.CompleteHelper;

import java.util.List;
import java.util.stream.Collectors;

public class BlueLibCMD extends BlueLibCommand {

	private static BlueLibCMD inst;

	private BlueLibCMD() {
		super(Main.inst, "bluelib");
	}

	public static void newInstance() {
		if (inst == null) {
			inst = new BlueLibCMD();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission("bluelib.admin")) return false;
		
		if (args.length == 1) {

			if (args[0].equalsIgnoreCase("reload")) {
				Main.inst.reload();
				sender.sendMessage("§aПлагин §2BlueLib§a успешно перезагружен.");
			}

			if (args[0].equalsIgnoreCase("forceSave")) {
				StorageEngine.forceCacheSave();
				sender.sendMessage("§aВыполнен принудительный сброс данных игроков на диск.");
			}

			return true;
		}

		if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
			if (PluginEngine.reloadPlugin(args[1])) {
				sender.sendMessage("§aПлагин §2".concat(args[1]).concat("§a успешно перезагружен."));
			} else {
				sender.sendMessage("§cПлагин §4".concat(args[1]).concat("§c не нейден или не зарегистрирован в ")
						.concat(Main.inst.getName()).concat("."));
			}

			return true;
		}

		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!sender.hasPermission("bluelib.admin")) return null;
		
		if (args.length == 1) {
			return CompleteHelper.filter(args, "reload", "forceSave");
		}

		if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
			return CompleteHelper.filter(
					args,
					PluginEngine.getRegisteredPlugins().values().stream()
							.map(BlueLibPlugin::getName).collect(Collectors.toList())
			);
		}
		
		return null;
	}

}

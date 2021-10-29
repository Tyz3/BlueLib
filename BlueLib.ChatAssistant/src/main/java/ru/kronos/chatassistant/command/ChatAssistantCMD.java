package ru.kronos.chatassistant.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.api.engine.PluginEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.util.CompleteHelper;
import ru.kronos.chatassistant.Main;

import java.util.List;

public final class ChatAssistantCMD extends BlueLibCommand {
	
	private static ChatAssistantCMD inst;

	private ChatAssistantCMD() {
		super(Main.inst, "chatassistant");
	}
	
	public static void newInstance() {
		if (inst == null) {
			inst = new ChatAssistantCMD();
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender.hasPermission("chatassistant.admin")) {
			if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				PluginEngine.reloadPlugin(Main.inst);
				sender.sendMessage("§a" + Main.inst.getName() + " успешно перезагружен!");
				return true;
			}
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender.hasPermission("chatassistant.admin")) {
			
			if (args.length == 1) {
				return CompleteHelper.filter(args, "reload");
			}
		}
		
		return null;
	}

}

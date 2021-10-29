package ru.kronos.bluelib.module.request;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.Message;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;

import java.util.List;

public class RequestCMD extends BlueLibCommand {

	private static RequestCMD inst;

	private RequestCMD() {
		super(Main.inst, "bluelib-request");
	}

	public static void newInstance() {
		if (inst == null) {
			inst = new RequestCMD();
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (args.length == 1) {
			if (OnlineEngine.isConsoleSender(sender)) return false;
			
			RequestTask rt = RequestSystem.getRequestTask(sender.getName(), args[0]);
			if (rt == null) {
				Message.RequestEngine_cancelRequest.send(sender);
				return true;
			}
			
			rt.run(sender.getName(), args[0]);
		}
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		
		return null;
	}

}

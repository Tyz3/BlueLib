package ru.kronos.bluelib.api.template.online;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandSender.Spigot;
import ru.kronos.bluelib.api.util.Placeholder;

import java.util.List;

public class BlueLibSender {

	protected CommandSender sender;
	
	public BlueLibSender(CommandSender sender) {
		this.sender = sender;
	}
	
	public CommandSender getCommandSender() {
		return sender;
	}
	
	public void sendTellRawMessage(String json, String... args) {
		spigot().sendMessage(ComponentSerializer.parse(Placeholder.set(json, args)));
		// 1710
//		OnlineManager.dispatchCommand("tellraw ".concat(sender.getName()).concat(" ").concat(json));
//		 ((CraftPlayer) ).getHandle().playerConnection.sendPacket(new PacketPlayOutChat(ChatSerializer.a(msg)));
	}
	
	public void sendTellRawMessage(TextComponent json, String... args) {
		spigot().sendMessage(ComponentSerializer.parse(Placeholder.set(ComponentSerializer.toString(json), args)));
	}
	
	public void sendTellRawMessage(TextComponent json) {
		spigot().sendMessage(json);
	}
	
	public void sendTellRawMessage(String json) {
		spigot().sendMessage(ComponentSerializer.parse(json));
	}
	
	public void sendMessage(String msg) {
		if (msg.startsWith("json: ")) {
			msg = msg.substring(5).trim();
			sendTellRawMessage(msg);
		} else sender.sendMessage(msg);
	}
	
	public void sendMessage(String msg, String... args) {
		sendMessage(Placeholder.set(msg, args));
	}
	
	public void sendMessages(String... msgs) {
		for (String msg : msgs) {
			sendMessage(msg);
		}
	}
	
	public void sendMessages(List<String> msgs, String... args) {
		msgs.forEach(msg -> sendMessage(msg, args));
	}
	
	public boolean isOp() {
		return sender.isOp();
	}
	
	public boolean hasPermission(String perm) {
		return sender.hasPermission(perm);
	}
	
	public String getName() {
		return sender.getName();
	}
	
	public Spigot spigot() {
		return sender.spigot();
	}
}

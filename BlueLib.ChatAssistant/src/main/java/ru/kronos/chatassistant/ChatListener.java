package ru.kronos.chatassistant;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.chatassistant.channel.ChatChannel;

public final class ChatListener implements Listener {

	private static ChatListener chatListener;

	private ChatListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);
	}

	public static void newInstance() {
		if (chatListener == null) {
			chatListener = new ChatListener();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
		
		char first = e.getMessage().toCharArray()[0];

		main: {
			switch (first) {
			case '!':
				if (e.getMessage().length() == 1) break;

				if (Permission.USE_SHOUT_CHAT.has(e.getPlayer())) {
					new ChatMessage(ChatChannel.SHOUT, e.getMessage().substring(1).trim(),
							OnlineEngine.getPlayer(e.getPlayer()), e.getRecipients()).build().send();
				} else Permission.USE_SHOUT_CHAT.sendHasntMessage(e.getPlayer());

				break main;
			case '$':
				if (e.getMessage().length() == 1) break;

				if (Permission.USE_TRADE_CHAT.has(e.getPlayer())) {
					new ChatMessage(ChatChannel.TRADE, e.getMessage().substring(1).trim(),
							OnlineEngine.getPlayer(e.getPlayer()), e.getRecipients()).build().send();
				} else Permission.USE_TRADE_CHAT.sendHasntMessage(e.getPlayer());

				break main;
			case '?':
				if (e.getMessage().length() == 1) break;

				if (Permission.USE_HELP_CHAT.has(e.getPlayer())) {
					new ChatMessage(ChatChannel.HELP, e.getMessage().substring(1).trim(),
							OnlineEngine.getPlayer(e.getPlayer()), e.getRecipients()).build().send();
				} else Permission.USE_HELP_CHAT.sendHasntMessage(e.getPlayer());

				break main;
			default: break;
			}

			new ChatMessage(ChatChannel.LOCAL, e.getMessage().substring(1).trim(),
					OnlineEngine.getPlayer(e.getPlayer()), e.getRecipients()).build().send();
		}

		e.setCancelled(true);
	}
	
}

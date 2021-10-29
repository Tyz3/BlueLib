package ru.kronos.bluelib;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.template.online.BlueLibSender;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public enum Message {

	GlobalSettings_DebugPrefix_MINIMUM,
	GlobalSettings_DebugPrefix_CRITICAL,
	GlobalSettings_DebugPrefix_ERROR,
	GlobalSettings_DebugPrefix_WARNING,
	GlobalSettings_DebugPrefix_INFO,
	GlobalSettings_DebugPrefix_DEBUG,

	OnlineEngine_kick, OnlineEngine_kickall,

	RequestEngine_cancelRequest, RequestEngine_jsonRequest, RequestEngine_defaultButtonTip,

	CooldownEngine_hasCooldown,

	StorageEngine_loadingData,

	WarmUpEngine_notification, WarmUpEngine_cancelled_move, WarmUpEngine_cancelled_hit,

	API3rdPartyEngine_DiscordSRV_linkedAccount, API3rdPartyEngine_DiscordSRV_unlinkedAccount,
	API3rdPartyEngine_DiscordSRV_unlinkedAccountPM;

	private static final int PLACEHOLDERS_LIMIT = 5;
	
	private String[] message;
	
	private static void initMsg(Message msg, Object obj) {
		
		if (obj == null) {
			System.err.println(Message.class.getCanonicalName() + " | Ошибка чтения параметра (null). Путь: '" + msg.name().replace("_", ".") + "'.");
			return;
		}
		
		if (obj instanceof List<?>) {
			List<String> list = (ArrayList<String>) obj;
			msg.message = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				msg.message[i] = list.get(i).replace("&", "§");
			}
		} else if (obj instanceof String) {
			msg.message = new String[] {obj.toString().replace("&", "§")};
		} else {
			System.err.println(Setting.class.getCanonicalName() + " | Ошибка определения типа параметра (" + obj + "). Путь: '" + msg.name().replace("_", ".") + "'.");
		}
	}
	
	public static void load(ConfigurationSection c) {
		for (Message msg : values()) {
			try {
				Object obj = c.get(msg.name().replace("_", "."));
				
				initMsg(msg, obj);
				
			} catch (NullPointerException e) {
				System.err.println(Setting.class.getCanonicalName() + " | NullPointerException. Path: '"+msg.name().replace("_", ".")+"'.");
				e.printStackTrace();
			} catch (ClassCastException e) {
				System.err.println(Setting.class.getCanonicalName() + " | ClassCastException. Path: '"+msg.name().replace("_", ".")+"'.");
				e.printStackTrace();
			}
		}
	}


	public void send(CommandSender sender) {
		new MessageSender().send(sender);
	}

	public void send(BlueLibSender p) {
		new MessageSender().send(p);
	}
	
	// Первая замена выполняется здесь, последующие в классе Sender
	public MessageSender replace(String from, String to) {
		MessageSender s = new MessageSender();
		s.replace(from, to);
		return s;
	}
	
	public MessageSender replace(String from, int to) {
		return replace(from, String.valueOf(to));
	}
	
	public MessageSender replace(String from, long to) {
		return replace(from, String.valueOf(to));
	}
	
	public MessageSender replace(String from, double to) {
		return replace(from, String.valueOf(to));
	}
	
	public class MessageSender {
		private final String[] cache = new String[PLACEHOLDERS_LIMIT*2];
		private int placePosition = 0;
		
		public MessageSender replace(String from, String to) {
			cache[placePosition] = from;
			cache[placePosition+1] = to;
			placePosition += 2;
			return this;
		}
		
		public MessageSender replace(String from, int to) {
			return replace(from, String.valueOf(to));
		}
		
		public MessageSender replace(String from, long to) {
			return replace(from, String.valueOf(to));
		}
		
		public MessageSender replace(String from, double to) {
			return replace(from, String.valueOf(to));
		}
		
		public void send(CommandSender sender) {
			if(sender == null) return;
			if (Message.this.message == null) return;

			for (String m : Message.this.message) {
				if(m.isEmpty()) continue;
				String s = placeholders(m);
				sender.sendMessage(s);
			}
		}
		
		public void send(BlueLibSender p) {
			if (p == null) return;
			if (Message.this.message == null) return;

			for (String s : Message.this.message) {
				if (placePosition == 0)
					p.sendMessage(s);
				else
					p.sendMessage(s, cache);
			}
		}

		private String placeholders(String input) {
			if (!input.contains("%") && !input.contains("{")) {
				return input;
			}
			
			if (placePosition != 0 && cache.length % 2 == 0 && cache.length > 1) {
				for (int i = 0; i < cache.length; i+=2) {
					if (cache[i] == null) break;
					input = input.replace(cache[i], cache[i+1]);
				}
			}
			
			return input;
		}
		
		public String get() {
			return placeholders(Message.this.message[0]);
		}
		
		public String[] gets() {
			String[] msgs = new String[Message.this.message.length];
			for (int i = 0; i < Message.this.message.length; i++) {
				msgs[i] = placeholders(Message.this.message[i]);
			}
			return msgs;
		}
	}
	
	public String get() {
		return Message.this.message[0];
	}
	
	public String[] gets() {
		return Message.this.message;
	}
}

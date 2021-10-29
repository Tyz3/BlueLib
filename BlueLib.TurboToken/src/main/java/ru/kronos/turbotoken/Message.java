package ru.kronos.turbotoken;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public enum Message {
	
	potions_started, potions_ended;

	private static final int PLACEHOLDERS_LIMIT = 5;

	private String[] message;
	
	public static void load(ConfigurationSection c) {
		for (Message m : values()) {
			m.message = null;
			Object obj = c.get(m.name().replace("_", "."));
			if (obj instanceof List<?>) {
				List<String> list = (ArrayList<String>) obj;
				m.message = new String[list.size()];
				for (int i = 0; i < list.size(); i++) {
					m.message[i] = list.get(i).replace("&", "§");
				}
			} else if (obj instanceof String) {
				m.message = new String[] {obj.toString().replace("&", "§")};
			}
		}
	}


	public void send(CommandSender sender) {
		new Sender().send(sender);
	}
	
	// Первая замена выполняется здесь, последующие в классе Sender
	public Sender replace(String from, String to) {
		Sender s = new Sender();
		s.replace(from, to);
		return s;
	}
	
	public Sender replace(String from, int to) {
		return replace(from, String.valueOf(to));
	}
	
	public Sender replace(String from, long to) {
		return replace(from, String.valueOf(to));
	}
	
	public Sender replace(String from, double to) {
		return replace(from, String.valueOf(to));
	}
	
	public class Sender {
		private final String[] cache = new String[PLACEHOLDERS_LIMIT*2];
		private int placePosition = 0;
		
		public Sender replace(String from, String to) {
			cache[placePosition] = from;
			cache[placePosition+1] = to;
			placePosition += 2;
			return this;
		}
		
		public Sender replace(String from, int to) {
			return replace(from, String.valueOf(to));
		}
		
		public Sender replace(String from, long to) {
			return replace(from, String.valueOf(to));
		}
		
		public Sender replace(String from, double to) {
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

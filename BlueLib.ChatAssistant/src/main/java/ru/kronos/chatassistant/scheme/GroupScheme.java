package ru.kronos.chatassistant.scheme;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public final class GroupScheme extends MessageScheme {

	public final String NAME;
	public final MessageScheme SCHEME;
	
	public GroupScheme(String name, ConfigurationSection c) {
		super(c);
		NAME = name;

		SCHEME = new MessageScheme(c);
	}
}

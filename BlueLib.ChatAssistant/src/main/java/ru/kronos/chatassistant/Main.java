package ru.kronos.chatassistant;

import ru.kronos.bluelib.api.engine.ConfigEngine;
import ru.kronos.bluelib.api.engine.StorageEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.api.template.config.BukkitConfig;
import ru.kronos.bluelib.api.template.storage.JSONPlayerStorage;
import ru.kronos.bluelib.api.template.storage.PlayerStorageTemplate;
import ru.kronos.chatassistant.command.*;

public final class Main extends BlueLibPlugin {

	public static Main inst;

	public static BukkitConfig config;

	@Override
	public void load() {
		inst = this;
		config = ConfigEngine.registerNewConfig(this, "config.yml");

		StorageEngine.registerNewPlayerStorageTemplate(new ChatAssistantStorage());
	}

	@Override
	public void enable() {
		ChatListener.newInstance();
		ChatAssistantCMD.newInstance();
		DoCMD.newInstance();
		MeCMD.newInstance();
		NonrpCMD.newInstance();
		ScreamCMD.newInstance();
		WhisperCMD.newInstance();
		BroadcastCMD.newInstance();
		TodoCMD.newInstance();

		config.reload();
		ChatEngine.getInstance().enable();
	}

	@Override
	public void disable() {
		ChatEngine.getInstance().disable();
	}

	private static class ChatAssistantStorage extends JSONPlayerStorage {

		public ChatAssistantStorage() {
			super(Main.inst.getPluginID(), StorageEngine.PLAYERS_DATA_FOLDER);
		}

		@Override
		public void saveData() {
			loadFile();

			getConfig().set("ChatAssistant.MessageColor", getPlayer().getHexMessageColor());
			getConfig().set("ChatAssistant.DisplayNameColor", getPlayer().getHexDisplayNameColor());
			getConfig().set("ChatAssistant.DiscordId", getPlayer().getDiscordId());

			getConfig().save();
		}

		@Override
		public void loadData() {
			loadFile();

			getPlayer().setMessageColor(getConfig().getString("ChatAssistant.MessageColor", null));
			getPlayer().setDisplayNameColor(getConfig().getString("ChatAssistant.DisplayNameColor", null));
		}

		@Override
		public PlayerStorageTemplate clone() {
			return new ChatAssistantStorage();
		}
	}

}

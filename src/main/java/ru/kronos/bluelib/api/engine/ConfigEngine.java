package ru.kronos.bluelib.api.engine;

import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.Message;
import ru.kronos.bluelib.Setting;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.api.template.config.BukkitConfig;

import java.util.HashMap;
import java.util.Map;

public final class ConfigEngine extends BlueLibEngine {

	private static ConfigEngine inst;

	public final static BukkitConfig CONFIG = BukkitConfig.create(Main.inst, "config.yml");
	public final static BukkitConfig MESSAGES = BukkitConfig.create(Main.inst, "messages.yml");
	
	private final static Map<String, BukkitConfig> CUSTOM_CONFIGS = new HashMap<>();

	private ConfigEngine() {}

	public static ConfigEngine getInstance() {
		return inst == null ? inst = new ConfigEngine() : inst;
	}

	@Override
	public void enable() {
		CONFIG.reload();
		MESSAGES.reload();

		CUSTOM_CONFIGS.values().forEach(BukkitConfig::reload);

		Setting.load(CONFIG.get());
		Message.load(MESSAGES.get());

		// Параметры оболочки
		Main.logLevel = LoggingLevel.matchOrDefault(CONFIG.get().getString("GlobalSettings.logLevel"), LoggingLevel.MINIMUM);
		Main.projectName = CONFIG.get().getString("GlobalSettings.projectName", "Реклама YOTA в названии проекта");

		LoggingLevel.MINIMUM.setPrefix(Message.GlobalSettings_DebugPrefix_MINIMUM.get());
		LoggingLevel.CRITICAL.setPrefix(Message.GlobalSettings_DebugPrefix_CRITICAL.get());
		LoggingLevel.ERROR.setPrefix(Message.GlobalSettings_DebugPrefix_ERROR.get());
		LoggingLevel.WARNING.setPrefix(Message.GlobalSettings_DebugPrefix_WARNING.get());
		LoggingLevel.INFO.setPrefix(Message.GlobalSettings_DebugPrefix_INFO.get());
		LoggingLevel.DEBUG.setPrefix(Message.GlobalSettings_DebugPrefix_DEBUG.get());

		LogEngine.debugMsg(LoggingLevel.MINIMUM, "§8GlobalSettings: ");
		LogEngine.debugMsg(LoggingLevel.MINIMUM, "§8 > Уровень логгирования: §6".concat(Main.logLevel.name()));
	}

	@Override
	public void disable() {}

	public static void reload() {
		getInstance().disable();
		getInstance().enable();
	}
	
	public static BukkitConfig registerNewConfig(BlueLibPlugin plugin, String configName) {
		BukkitConfig config = BukkitConfig.create(plugin.getPlugin(), plugin.getPlugin().getDataFolder(), configName);
		CUSTOM_CONFIGS.put(plugin.getPluginID().toString().concat(configName), config);
		return config;
	}
	
	public static void unregisterConfig(BlueLibPlugin plugin, String configName) {
		CUSTOM_CONFIGS.remove(plugin.getPluginID().toString().concat(configName)).save();
	}

	public static String getAbsolutePathToBlueLibFolder() {
		return Main.inst.getDataFolder().getAbsolutePath();
	}
	
	public static void reloadConfig(String configName) {
		CUSTOM_CONFIGS.get(configName).reload();
	}
	
	public static void saveConfig(String configName) {
		CUSTOM_CONFIGS.get(configName).save();
	}
	
	public static BukkitConfig getMainConfig() {
		return CONFIG;
	}
	
	public static BukkitConfig getMessageConfig() {
		return MESSAGES;
	}
}

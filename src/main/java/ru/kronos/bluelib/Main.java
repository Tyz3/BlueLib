package ru.kronos.bluelib;

import org.bukkit.plugin.java.JavaPlugin;
import ru.kronos.bluelib.api.engine.*;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.extra.PluginID;
import ru.kronos.bluelib.module.chestmenu.ContainerTesting;
import ru.kronos.bluelib.module.scoreboard.ScoreboardTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Main extends JavaPlugin {
	
	/*
		
	88888888888 888      d8b          888                                      .d8888b.  888                              888    
	    888     888      Y8P          888                                     d88P  Y88b 888                              888    
	    888     888                   888                                     Y88b.      888                              888    
	    888     88888b.  888 88888b.  888  888  .d88b.  888d888 .d8888b        "Y888b.   888888 888d888 888  888  .d8888b 888888 
	    888     888 "88b 888 888 "88b 888 .88P d8P  Y8b 888P"   88K               "Y88b. 888    888P"   888  888 d88P"    888    
	    888     888  888 888 888  888 888888K  88888888 888     "Y8888b.            "888 888    888     888  888 888      888    
	    888     888  888 888 888  888 888 "88b Y8b.     888          X88      Y88b  d88P Y88b.  888     Y88b 888 Y88b.    Y88b.  
	    888     888  888 888 888  888 888  888  "Y8888  888      88888P'       "Y8888P"   "Y888 888      "Y88888  "Y8888P  "Y888 
	    
	*/
	
	public static Main inst;

	public static LoggingLevel logLevel = LoggingLevel.MINIMUM;
	public static String projectName = "";
	public static final PluginID PLUGIN_ID = PluginID.randomID();
	
	private static final List<BlueLibEngine> engines = new ArrayList<>();

	public Main() {
		inst = this;
		ConfigEngine.getInstance().enable(); // Загрузка всех конфигураций
	}
	
	@Override
	public void onLoad() {
		long t = System.currentTimeMillis();

															engines.add(LogEngine.getInstance());
		if (Setting.OnlineEngine_enabled.getBool())			engines.add(OnlineEngine.getInstance());
		if (Setting.CooldownEngine_enabled.getBool())		engines.add(CooldownEngine.getInstance());
		if (Setting.RequestEngine_enabled.getBool())		engines.add(RequestEngine.getInstance());
		if (Setting.WarmUpEngine_enabled.getBool())			engines.add(WarmUpEngine.getInstance());
		if (Setting.EventEngine_enabled.getBool())			engines.add(EventEngine.getInstance());
		if (Setting.EnchantEngine_enabled.getBool())		engines.add(EnchantEngine.getInstance());
		if (Setting.PotionEngine_enabled.getBool())			engines.add(PotionEngine.getInstance());
		if (Setting.DamageControlEngine_enabled.getBool())	engines.add(DamageControlEngine.getInstance());
		if (Setting.CommandEngine_enabled.getBool())		engines.add(CommandEngine.getInstance());
		if (Setting.ChestMenuEngine_enabled.getBool())		engines.add(ChestMenuEngine.getInstance());
		if (Setting.ScoreboardEngine_enabled.getBool())		engines.add(ScoreboardEngine.getInstance());
		if (Setting.StorageEngine_enabled.getBool())		engines.add(StorageEngine.getInstance());
		if (Setting.PluginEngine_enabled.getBool())			engines.add(PluginEngine.getInstance());
															engines.add(API3rdPartyEngine.getInstance());

		LogEngine.debugMsg(LoggingLevel.MINIMUM, "§8[[ §6Проект §o", projectName, " §8]]");
		LogEngine.debugMsg(LoggingLevel.INFO,
				"§eРегистрация модулей (", engines.size(), "/", BlueLibEngine.getRegisteredAmount() - 1,
				") завершилась за ", System.currentTimeMillis() - t, " мс."
		);

	}
	
	@Override
	public void onEnable() {
		enable();

		StringJoiner joiner = new StringJoiner("§8, ");
		for (BlueLibEngine e : engines) {
			joiner.add((e.isEnabled() ? "§a" : "§c").concat(e.toString()));
		}
		LogEngine.debugMsg(LoggingLevel.INFO, "[", joiner.toString(), "]");

		// Кастомные загрузки
		ContainerTesting.make();
		ScoreboardTesting.make();

//		StorageTesting.make();
//		PotionTesting.make();
	}
	
	@Override
	public void onDisable() {
		disable();
	}

	public void enable() {
		long t = System.currentTimeMillis();
		LogEngine.debugMsg(LoggingLevel.DEBUG, "§eЗапуск модулей...");

		for (BlueLibEngine engine : engines) {
			if (engine.isEnabled()) continue;

			long t2 = System.currentTimeMillis();
			engine.enable();
			engine.setEnabled(true);
			LogEngine.debugMsg(LoggingLevel.DEBUG, " §8> §a", engine.getClass().getSimpleName(), "§2 загрузился за §a", System.currentTimeMillis() - t2, " мс§2.");
		}

		LogEngine.debugMsg(LoggingLevel.INFO, "§eЗапуск модулей завершился за ", System.currentTimeMillis() - t, " мс.");
	}

	public void disable() {
		long t = System.currentTimeMillis();
		LogEngine.debugMsg(LoggingLevel.DEBUG, "§eОтключение модулей...");

		for (BlueLibEngine engine : engines) {
			if (!engine.isEnabled()) continue;

			long t2 = System.currentTimeMillis();
			engine.disable();
			engine.setEnabled(false);
			LogEngine.debugMsg(LoggingLevel.DEBUG, " §8> §a", engine.getClass().getSimpleName(), "§2 отключен за §a", System.currentTimeMillis() - t2, " мс§2.");
		}

		LogEngine.debugMsg(LoggingLevel.INFO, "§eОтключение модулей завершилось за ", System.currentTimeMillis() - t, " мс.");
	}
	
	public void reload() {
		long t = System.currentTimeMillis();
		
		ConfigEngine.reload(); // Перезагрузка глобальных настроек.

		LogEngine.debugMsg(LoggingLevel.DEBUG, "§eПерезагрузка модулей...");
		
		disable();
		enable();

		LogEngine.debugMsg(LoggingLevel.INFO, "§eПерезагрузка модулей завершилась за ", System.currentTimeMillis() - t, " мс.");
	}
}

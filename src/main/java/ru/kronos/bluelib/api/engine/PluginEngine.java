package ru.kronos.bluelib.api.engine;

import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.extra.PluginID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PluginEngine extends BlueLibEngine {

	private static PluginEngine inst;

	private static final Map<PluginID, BlueLibPlugin> PLUGINS = new HashMap<>();

	private PluginEngine() {}

	public static PluginEngine getInstance() {
		return inst == null ? inst = new PluginEngine() : inst;
	}
	
	@Override
	public void enable() {
		// Выполняется при регистрации нового плагина: при вызове registerNewPlugin().
		for (BlueLibPlugin plugin : PLUGINS.values()) {
			long t = System.currentTimeMillis();

			plugin.enable();

			LogEngine.debugMsg(LoggingLevel.DEBUG, " §8> > §5Плагин §d", plugin.getName(), "§5 загрузился за ", System.currentTimeMillis() - t, " мс.");
		}

		setEnabled(true);
	}
	
	@Override
	public void disable() {
		long t = System.currentTimeMillis();
		
		PLUGINS.values().forEach(BlueLibPlugin::disable);

		LogEngine.debugMsg(LoggingLevel.INFO, "§eВсе плагины выполнили фазу DISABLE за ", System.currentTimeMillis() - t, " мс.");
		setEnabled(false);
	}

	public static void reloadPlugin(BlueLibPlugin plugin) {
		plugin.disable();
		plugin.enable();
	}

	public static boolean reloadPlugin(String pluginName) {
		if (PLUGINS.values().stream().map(BlueLibPlugin::getName).collect(Collectors.toList()).contains(pluginName)) {
			PLUGINS.values().stream().filter(p -> p.getName().equalsIgnoreCase(pluginName))
					.forEach(PluginEngine::reloadPlugin);
			return true;
		}

		return false;
	}
	
	/**
	 * Регистрирует плагин в BlueLib и выполняет метод enable().
	 * @param plugin - главный класс твоего нового неотразимого творения :)
	 */
	public static void registerNewPlugin(BlueLibPlugin plugin) {
		long t = System.currentTimeMillis();

		LogEngine.debugMsg(LoggingLevel.DEBUG, "§eНачалась регистрация плагина ", plugin.getName(), "...");

		if (!plugin.getLicense().isValid(plugin)) {
			LogEngine.debugMsg(LoggingLevel.CRITICAL, "§4 !! Плагин ", plugin.getPlugin().getName(), " не был зарегистрирован, поскольку его лицензия не действительна.");
			return;
		}

		PLUGINS.put(plugin.getPluginID(), plugin);
		plugin.enable();

		LogEngine.debugMsg(LoggingLevel.INFO, "§eПлагин ", plugin.getPlugin().getName(), " зарегистрирован и выполнил фазу ENABLE за ", System.currentTimeMillis() - t, " мс.");
	}

	public static Map<PluginID, BlueLibPlugin> getRegisteredPlugins() {
		return PLUGINS;
	}
	
	
}

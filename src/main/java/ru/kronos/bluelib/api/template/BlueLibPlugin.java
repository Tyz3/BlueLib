package ru.kronos.bluelib.api.template;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.ConfigEngine;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.PluginEngine;
import ru.kronos.bluelib.extra.PluginID;
import ru.kronos.bluelib.license.CryptoUtil;
import ru.kronos.bluelib.license.License;

public abstract class BlueLibPlugin extends JavaPlugin {

	private final PluginID pluginID = PluginID.randomID();
	private License license;
	
	public BlueLibPlugin() {
		String lic = ConfigEngine.CONFIG.get().getString("PluginEngine.Licenses.".concat(getName()));

		if (lic != null && !lic.equals("вставь_сюда_лицензионный_ключ")) {
			putLicense(lic);
		} else {
			ConfigEngine.CONFIG.get().set("PluginEngine.Licenses.".concat(getName()), "вставь_сюда_лицензионный_ключ");
			ConfigEngine.CONFIG.save();
			LogEngine.debugMsg(LoggingLevel.WARNING, "§cЛицензия на плагин ", getName(), " не обнаружена в PluginEngine.Licenses.", getName());
		}
	}

	private void putLicense(String license) {
		putLicense(CryptoUtil.hex2Byte(license));
	}
	
	private void putLicense(byte[] license) {
		this.license = new License(license, this);
	}

	@Override
	public void onLoad() {
		if (license == null) {
			LogEngine.debugMsg(LoggingLevel.WARNING, "Плагин ", getPlugin().getName(), " не имеет лицензии. Загрузка отменена.");
		} else {
			load();
		}
	}

	@Override
	public void onEnable() {
		if (license == null) {
			LogEngine.debugMsg(LoggingLevel.WARNING, "Плагин ", getPlugin().getName(), " не имеет лицензии. Регистрация отменена.");

			Bukkit.getPluginManager().disablePlugin(this);
		} else {
			PluginEngine.registerNewPlugin(this);
		}
	}

	public abstract void load();
	public abstract void enable();
	public abstract void disable();
	
	public JavaPlugin getPlugin() {
		return this;
	}
	
	public PluginID getPluginID() {
		return pluginID;
	}
	
	public License getLicense() {
		return license;
	}
}

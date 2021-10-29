package ru.kronos.combatlog;

import ru.kronos.bluelib.api.engine.ConfigEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.api.template.config.BukkitConfig;
import ru.kronos.combatlog.command.CombatCMD;

public class Main extends BlueLibPlugin {
	
	public static Main inst;
	
	public static BukkitConfig mainConfig;
	
	@Override
	public void load() {
		inst = this;
		mainConfig = ConfigEngine.registerNewConfig(this, "config.yml");
	}

	@Override
	public void enable() {
		mainConfig.reload();
		CombatCMD.newInstance();
		CombatLogEngine.getInstance().enable();
		Task.getInstance().enable();
		CombatLogListener.getInstance().enable();
	}

	@Override
	public void disable() {
		Task.getInstance().disable();
		CombatLogListener.getInstance().disable();
		CombatLogEngine.getInstance().disable();
	}
	
	
}

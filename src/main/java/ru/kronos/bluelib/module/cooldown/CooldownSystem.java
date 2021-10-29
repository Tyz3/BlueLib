package ru.kronos.bluelib.module.cooldown;

import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.CooldownEngine;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.extra.PluginID;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownSystem implements Runnable {
	
	public static Map<PluginID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();
	
	@Override
	public void run() {
		// Задание очистки.
		long now = System.currentTimeMillis();
		
		int var1 = 0;
		
		for (PluginID pid : cooldowns.keySet()) {
			Map<String, Long> hm = cooldowns.getOrDefault(pid, new ConcurrentHashMap<>());
			
			if (hm.isEmpty()) continue;
			
			for (Entry<String, Long> e : hm.entrySet()) {
				if (now < e.getValue()) continue;
				hm.remove(e.getKey());
				var1--;
			}
		}

		LogEngine.debugMsg(LoggingLevel.DEBUG, CooldownEngine.class.getSimpleName(), " | Выполнена очистка кулдаунов (", String.valueOf(var1), ").");
	}
	
	
}

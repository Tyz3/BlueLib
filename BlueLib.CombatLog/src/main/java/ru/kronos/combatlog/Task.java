package ru.kronos.combatlog;

import org.bukkit.scheduler.BukkitTask;

import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibTask;
import ru.kronos.bluelib.api.util.ThreadDaemon;

import java.util.Map;
import java.util.UUID;

public class Task extends BlueLibTask {
	
	private BukkitTask task;

	private static Task inst;

	private Task() {}

	public static Task getInstance() {
		return inst == null ? inst = new Task() : inst;
	}

	@Override
	public void enable() {
		task = ThreadDaemon.asyncTimer(this, 0L, 10L);
	}

	@Override
	public void disable() {
		ThreadDaemon.cancelTask(task);
	}
	
	@Override
	public void run() {
		for (Map.Entry<UUID, Long> e : CombatLogEngine.playersInCombat.entrySet()) {
			if (e.getValue() < System.currentTimeMillis()) {
				CombatLogEngine.playersInCombat.remove(e.getKey());

				Message.combatExpired.send(OnlineEngine.getPlayer(e.getKey()).getBukkitPlayer());
			}
		}
	}
}

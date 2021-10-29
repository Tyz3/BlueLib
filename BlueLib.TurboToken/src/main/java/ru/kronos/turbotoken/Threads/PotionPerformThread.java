package ru.kronos.turbotoken.Threads;

import org.bukkit.scheduler.BukkitTask;
import ru.kronos.bluelib.api.util.ThreadDaemon;
import ru.kronos.turbotoken.Main;
import ru.kronos.turbotoken.Setting;

public class PotionPerformThread implements Runnable {
	
	private BukkitTask task;
	
	public void reload() {
		ThreadDaemon.cancelTask(task);
		task = ThreadDaemon.asyncTimer(this, 0, Setting.effectsApplyEveryTicks.getLong());
	}
	
	@Override
	public void run() {
		Main.ONLINE.forEach((p, o) -> o.tickPotions());
	}
	
}

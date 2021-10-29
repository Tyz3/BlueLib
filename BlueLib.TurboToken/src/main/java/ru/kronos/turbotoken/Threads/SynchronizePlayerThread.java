package ru.kronos.turbotoken.Threads;

import org.bukkit.scheduler.BukkitTask;
import ru.kronos.bluelib.api.util.ThreadDaemon;
import ru.kronos.turbotoken.Main;
import ru.kronos.turbotoken.Setting;

public class SynchronizePlayerThread implements Runnable {

	private static BukkitTask task;
	
	
	public void reload() {
		ThreadDaemon.cancelTask(task);
		task = ThreadDaemon.asyncTimer(this, 20, Setting.syncPlayerTaskDelay.getLong());
	}
	
	@Override
	public void run() {
		Main.ONLINE.forEach((p, o) -> {o.synchronize(); o.changeSpeedMovement();});
	}
}

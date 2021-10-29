package ru.kronos.bluelib.api.util;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import ru.kronos.bluelib.Main;

public class ThreadDaemon {
	
	private static final BukkitScheduler shed = Bukkit.getScheduler();
	
	public static BukkitTask sync(Runnable r) {
		return shed.runTask(Main.inst, r);
	}
	
	public static BukkitTask syncTimer(Runnable r, long start, long repeate) {
		return shed.runTaskTimer(Main.inst, r, start, repeate);
	}
	
	public static BukkitTask syncLater(Runnable r, long delay) {
		return shed.runTaskLater(Main.inst, r, delay);
	}
	
	public static BukkitTask async(Runnable r) {
		return shed.runTaskAsynchronously(Main.inst, r);
	}
	
	public static BukkitTask asyncTimer(Runnable r, long start, long repeate) {
		return shed.runTaskTimerAsynchronously(Main.inst, r, start, repeate);
	}
	
	public static BukkitTask asyncLater(Runnable r, long delay) {
		return shed.runTaskLaterAsynchronously(Main.inst, r, delay);
	}
	
	public static void cancelTask(BukkitTask task) {
		if (task != null) shed.cancelTask(task.getTaskId());
	}
}

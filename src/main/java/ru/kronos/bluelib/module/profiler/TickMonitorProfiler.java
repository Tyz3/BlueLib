package ru.kronos.bluelib.module.profiler;

import org.bukkit.scheduler.BukkitTask;
import ru.kronos.bluelib.api.util.ThreadDaemon;

import java.util.*;

public class TickMonitorProfiler {
	
	private static int tick = -1;
	private static Deque<Long> times = new ArrayDeque<>();
	private static BurstProfiler burst;
	private static List<SampleSummary> reports = new ArrayList<>(15);
	private static long tickMinimum = 100;
	private static BukkitTask task;
	
	public static void setTickMinimum(long tickMinimum) {
		TickMonitorProfiler.tickMinimum = tickMinimum;
	}
	
	public static void start() {
		if (tick != -1) {
			return;
		}
		burst = new BurstProfiler(10000);
		burst.start();
		tick = 0;
		task = ThreadDaemon.syncTimer(() -> {
			if (times.size() >= 999) {
				times.poll();
			}
			tick++;
			long current = System.currentTimeMillis();
			if (times.size() > 0) {
				long time = times.getLast();
				if (current - time >= tickMinimum) {
					SampleSummary summary = burst.getSummary(time);
					if (reports.size() >= 15) {
						reports.stream().min(Comparator.comparingLong(SampleSummary::getDuration)).ifPresent(s -> {
							reports.remove(s);
						});
					}
					reports.add(summary);
				}
			}
			times.add(current);
		}, 1L, 1L);
	}
	
	public static void stop() {
		ThreadDaemon.cancelTask(task);
		tick = -1;
		burst.stop();
	}
	
	public static void clear() {
		reports.clear();
	}
	
	public static List<SampleSummary> getReports() {
		return reports;
	}
	
}

package ru.kronos.bluelib.api.engine;

import org.bukkit.scheduler.BukkitTask;
import ru.kronos.bluelib.Setting;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.util.CountingPattern;
import ru.kronos.bluelib.extra.PluginID;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.warmup.WarmUpTask;
import ru.kronos.bluelib.api.util.ThreadDaemon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class WarmUpEngine extends BlueLibEngine implements Runnable {
	
	private static WarmUpEngine inst;

	// String = "playerName-actionName"
	public static Map<PluginID, Map<String, WarmUpTask>> warmups = new ConcurrentHashMap<>();

	private static BukkitTask task;

	private WarmUpEngine() {}

	public static WarmUpEngine getInstance() {
		return inst == null ? inst = new WarmUpEngine() : inst;
	}
	
	@Override
	public void enable() {
		task = ThreadDaemon.syncTimer(this, 0L, 20L);
		setEnabled(true);
	}
	
	@Override
	public void disable() {
		ThreadDaemon.cancelTask(task);
		warmups.clear();
		setEnabled(false);
	}

	public static void startWarmUp(PluginID pid, BlueLibPlayer player, String actionName, Runnable warmupAction, long delaySeconds, boolean canMove, CountingPattern pattern) {
		if (player == null || player.getBukkitPlayer() == null || !player.getBukkitPlayer().isOnline()) return;

//		if (player.isOp() || player.hasPermission("bluelib.warmup.all.bypass")) {
//			warmupAction.run();
//			return;
//		}

		Map<String, WarmUpTask> hm = warmups.getOrDefault(pid, new ConcurrentHashMap<>());
		hm.put(
				player.getName().concat("-").concat(actionName),
				new WarmUpTask(warmupAction, player, delaySeconds, pattern, canMove)
		);

		if (!warmups.containsKey(pid)) {
			warmups.put(pid, hm);
		}
	}

	// Перегрузка с playerName.

	public static void startWarmUp(PluginID pid, String playerName, String actionName, Runnable warmupAction, long delaySeconds, boolean canMove, CountingPattern countingPattern) {
		startWarmUp(pid, OnlineEngine.getPlayer(playerName), actionName, warmupAction, delaySeconds, canMove, countingPattern);
	}
	
	public static void startWarmUp(PluginID pid, String playerName, String actionName, Runnable warmupAction, long delaySeconds, boolean canMove) {
		startWarmUp(pid, playerName, actionName, warmupAction, delaySeconds, canMove, Setting.WarmUpEngine_countingPattern.getString());
	}
	
	public static void startWarmUp(PluginID pid, String playerName, String actionName, Runnable warmupAction, long delaySeconds, boolean canMove, String countingPattern) {
		startWarmUp(pid, OnlineEngine.getPlayer(playerName), actionName, warmupAction, delaySeconds, canMove, new CountingPattern(countingPattern));
	}
	
	// Перегрузка с YNPlayer.
	
	public static void startWarmUp(PluginID pid, BlueLibPlayer player, String actionName, Runnable warmupAction, long delaySeconds, boolean canMove, String countingPattern) {
		startWarmUp(pid, player, actionName, warmupAction, delaySeconds, canMove, new CountingPattern(countingPattern));
	}
	
	public static void startWarmUp(PluginID pid, BlueLibPlayer player, String actionName, Runnable warmupAction, long delaySeconds, boolean canMove) {
		startWarmUp(pid, player, actionName, warmupAction, delaySeconds, canMove, Setting.WarmUpEngine_countingPattern.getString());
	}

	@Override
	public void run() {

		for (PluginID pid : warmups.keySet()) {

			Map<String, WarmUpTask> hm = warmups.get(pid);

			if (hm == null || hm.isEmpty()) continue;

			for (Map.Entry<String, WarmUpTask> e : hm.entrySet()) {

				if (e.getValue().isEnded())	{
					hm.remove(e.getKey());
					continue;
				}

				e.getValue().run();
			}
		}
	}
	
}

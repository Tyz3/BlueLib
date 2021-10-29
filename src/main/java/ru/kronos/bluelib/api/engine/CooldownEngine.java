package ru.kronos.bluelib.api.engine;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.Message;
import ru.kronos.bluelib.Setting;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.extra.PluginID;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.template.online.BlueLibSender;
import ru.kronos.bluelib.module.cooldown.CooldownSystem;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.bluelib.api.util.ThreadDaemon;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class CooldownEngine extends BlueLibEngine {

	private static CooldownEngine inst;
	
	private static BukkitTask clearTask;
	private static final CooldownSystem cooldownSystem = new CooldownSystem();

	private CooldownEngine() {}

	public static CooldownEngine getInstance() {
		return inst == null ? inst = new CooldownEngine() : inst;
	}
	
	@Override
	public void enable() {
		long clearTaskSecondsDelay = Setting.CooldownEngine_clearTaskDelaySeconds.getLong();

		clearTask = ThreadDaemon.asyncTimer(cooldownSystem, 0, 20L * clearTaskSecondsDelay);
	}
	
	@Override
	public void disable() {
		ThreadDaemon.cancelTask(clearTask);
		CooldownSystem.cooldowns.clear();
	}
	
	public static void sendDefaultCooldownMessage(PluginID pid, CommandSender sender, String actionName) {
		Message.CooldownEngine_hasCooldown.replace("{S}", CooldownEngine.getCooldown(Main.PLUGIN_ID, sender, actionName)).send(sender);
	}
	
	public static void sendDefaultCooldownMessage(PluginID pid, BlueLibPlayer sender, String actionName) {
		Message.CooldownEngine_hasCooldown.replace("{S}", CooldownEngine.getCooldown(Main.PLUGIN_ID, sender, actionName)).send(sender);
	}
	
	/**
	 * Устанавливает кулдаун на какое-то действие игрока.
	 * @param pid ид плагина, от которого идёт вызов этого метода.
	 * @param actionName название действия, на которое устанавливается кулдаун.
	 * @param sender целевой игрок.
	 * @param cooldown время в секундах.
	 */
	public static void setCooldown(PluginID pid, String actionName, BlueLibSender sender, long cooldown) {
		setCooldown(pid, actionName, sender.getName(), cooldown);
	}
	
	/**
	 * Устанавливает кулдаун на какое-то действие игрока.
	 * @param pid ид плагина, от которого идёт вызов этого метода.
	 * @param actionName название действия, на которое устанавливается кулдаун.
	 * @param sender целевой игрок.
	 * @param cooldown время в секундах.
	 */
	public static void setCooldown(PluginID pid, String actionName, CommandSender sender, long cooldown) {
		setCooldown(pid, actionName, sender.getName(), cooldown);
	}
	
	/**
	 * Устанавливает кулдаун на какое-то действие игрока.
	 * @param pid ид плагина, от которого идёт вызов этого метода.
	 * @param actionName название действия, на которое устанавливается кулдаун.
	 * @param playerName целевой игрок.
	 * @param cooldown время в секундах.
	 */
	public static void setCooldown(PluginID pid, String actionName, String playerName, long cooldown) {
		long expires = System.currentTimeMillis() + cooldown*1000;
		Map<String, Long> hm = CooldownSystem.cooldowns.getOrDefault(pid, new ConcurrentHashMap<>());
		hm.put(playerName.concat("-").concat(actionName), expires);
		
		if (!CooldownSystem.cooldowns.containsKey(pid))
			CooldownSystem.cooldowns.put(pid, hm);
	}
	
	public static boolean hasCooldown(PluginID pid, BlueLibSender sender, String actionName) {
		return hasCooldown(pid, sender.getName(), actionName);
	}
	
	public static boolean hasCooldown(PluginID pid, CommandSender sender, String actionName) {
		return hasCooldown(pid, sender.getName(), actionName);
	}
	
	public static boolean hasCooldown(PluginID pid, String playerName, String actionName) {
		Map<String, Long> hm = CooldownSystem.cooldowns.get(pid);
		if (hm == null || hm.isEmpty()) return false;
		String playerAction = playerName.concat("-").concat(actionName);
		if (!hm.containsKey(playerAction)) return false;
		return System.currentTimeMillis() < hm.get(playerAction);
	}
	
	/**
	 * @param pid ид плагина, от которого идёт вызов этого метода.
	 * @param sender целевой игрок.
	 * @return возвращает оставщееся время кулдауна.
	 */
	public static double getCooldown(PluginID pid, BlueLibSender sender, String actionName) {
		return getCooldown(pid, sender.getName(), actionName);
	}
	
	/**
	 * @param pid ид плагина, от которого идёт вызов этого метода.
	 * @param sender целевой игрок.
	 * @return возвращает оставщееся время кулдауна.
	 */
	public static double getCooldown(PluginID pid, CommandSender sender, String actionName) {
		return getCooldown(pid, sender.getName(), actionName);
	}
	
	/**
	 * @param pid ид плагина, от которого идёт вызов этого метода.
	 * @param playerName целевой игрок.
	 * @return возвращает оставщееся время кулдауна.
	 */
	public static double getCooldown(PluginID pid, String playerName, String actionName) {
		Map<String, Long> hm = CooldownSystem.cooldowns.get(pid);
		if (hm == null || hm.isEmpty()) return 0;
		String playerAction = playerName.concat("-").concat(actionName);
		if (!hm.containsKey(playerAction)) return 0;
		long cooldown = hm.get(playerAction) - System.currentTimeMillis();
		return MathOperation.roundAvoid((double)(cooldown <= 0 ? 0 : cooldown)/1000, 2);
	}
	
}

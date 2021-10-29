package ru.kronos.combatlog;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import org.bukkit.entity.Player;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.util.MathOperation;

public class CombatLogEngine extends BlueLibEngine {
	
	public static final Map<UUID, Long> playersInCombat = new ConcurrentHashMap<>();
	
	private static int duration;
	public static String bypassPermission;
	public static boolean kickExempt;
	public static boolean mobsExempt;
	public static boolean playersExempt;
	public static Set<String> allowedCmds = new HashSet<>();
	public static Set<World> disabledWorlds = new HashSet<>();
	public static Set<World> mobsCombatAllowedWorlds = new HashSet<>();
	public static boolean disableTeleports;
	public static int maxTeleportDistance;

	private static CombatLogEngine inst;

	private CombatLogEngine() {}

	public static CombatLogEngine getInstance() {
		return inst == null ? inst = new CombatLogEngine() : inst;
	}

	@Override
	public void enable() {
		Message.load(Main.mainConfig.getConfigurationSection("Messages"));

		ConfigurationSection c = Main.mainConfig.getConfigurationSection("Options");

		duration = c.getInt("duration", 5);
		bypassPermission = c.getString("bypassPermission", "combatlog.leave");
		kickExempt = c.getBoolean("kickExempt", true);
		mobsExempt = c.getBoolean("mobsExempt", false);
		playersExempt = c.getBoolean("playersExempt", false);
		disableTeleports = c.getBoolean("Blocking.disableTeleports", false);
		maxTeleportDistance = c.getInt("maxTeleportDistance", 0);

		allowedCmds = Set.copyOf(c.getStringList("Blocking.allowedCommands"));

		List<String> disWorlds = c.getStringList("Blocking.mobsAllowedCombatOverrideWorlds");
		for (String dw : disWorlds) {
			World w = Bukkit.getWorld(dw);
			if (w == null) continue;
			mobsCombatAllowedWorlds.add(w);
		}

		List<String> mobsAllowedWorlds = c.getStringList("Blocking.disabledWorlds");
		for (String dw : mobsAllowedWorlds) {
			World w = Bukkit.getWorld(dw);
			if (w == null) continue;
			disabledWorlds.add(w);
		}
	}

	@Override
	public void disable() {
		allowedCmds.clear();
		mobsCombatAllowedWorlds.clear();
		disabledWorlds.clear();
	}
	
	public static boolean isDisabledWorld(World w) {
		return disabledWorlds.contains(w);
	}
	
	public static boolean isAllowedMobsWorld(World w) {
		return mobsCombatAllowedWorlds.contains(w);
	}
	
	public static boolean hasCombat(Player p) {
		return !p.hasPermission(CombatLogEngine.bypassPermission) &&
				playersInCombat.containsKey(p.getUniqueId()) &&
				System.currentTimeMillis() < playersInCombat.get(p.getUniqueId());
	}

	public static void claimCombatWithPlayer(Player damager, Player victim) {
		disableFly(damager); disableFly(victim);
		
		if (!playersInCombat.containsKey(damager.getUniqueId())) {
			Message.combatClaimed
					.replace("{living}", victim.getDisplayName())
					.replace("{duration}", duration)
					.send(damager);
		}
		
		playersInCombat.put(damager.getUniqueId(), System.currentTimeMillis() + duration * 1000L);
	}
	
	public static void claimCombatWithMob(Player damager) {
		disableFly(damager);
		
		if (!playersInCombat.containsKey(damager.getUniqueId())) {
			Message.combatClaimed
					.replace("{living}", Message.defaultDamager.get())
					.replace("{duration}", duration)
					.send(damager);
		}
		
		playersInCombat.put(damager.getUniqueId(), System.currentTimeMillis() + duration * 1000L);
	}
	
	public static void checkCombat(Player p) {
		if (hasCombat(p)) {
			Message.combatCheck
					.replace("{seconds}", MathOperation.roundAvoid(
							(playersInCombat.get(p.getUniqueId()) - System.currentTimeMillis()) / 1000D, 2)
					)
					.send(p);
		} else {
			Message.combatNotExists.send(p);
		}
	}
	
	public static void removeCombat(Player p) {
		playersInCombat.remove(p.getUniqueId());
	}
	
	public static void disableFly(Player p) {
		p.setFlying(false);
		p.setAllowFlight(false);
	}
}

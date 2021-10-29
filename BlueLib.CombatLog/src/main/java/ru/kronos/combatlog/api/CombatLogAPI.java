package ru.kronos.combatlog.api;

import org.bukkit.entity.Player;

import ru.kronos.combatlog.CombatLogEngine;

public class CombatLogAPI {
	
	public static boolean hasCombat(Player player) {
		return CombatLogEngine.hasCombat(player);
	}

	public static void claimCombatWithPlayer(Player damager, Player victim) {
		CombatLogEngine.claimCombatWithPlayer(damager, victim);
	}

	public static void claimCombatWithMob(Player damager) {
		CombatLogEngine.claimCombatWithMob(damager);
	}
}

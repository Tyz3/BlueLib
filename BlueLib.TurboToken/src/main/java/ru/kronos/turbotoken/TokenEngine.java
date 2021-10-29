package ru.kronos.turbotoken;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.module.customevent.*;
import ru.kronos.turbotoken.Commands.TokenCMD;
import ru.kronos.turbotoken.Dictionary.TokenEnchant;
import ru.kronos.turbotoken.Dictionary.TokenTalisman;
import ru.kronos.turbotoken.Threads.PotionPerformThread;
import ru.kronos.turbotoken.Threads.SynchronizePlayerThread;

import java.util.Map.Entry;

public class TokenEngine {
	
	public static PotionPerformThread customPotionManager = new PotionPerformThread();
	public static SynchronizePlayerThread synchronizePlayerTask = new SynchronizePlayerThread();
	public static TokenListener enchantsListener = new TokenListener();
	public static TokenCMD tokenCMD = new TokenCMD();
	
	public static void reload() {
		synchronizePlayerTask.reload();
		customPotionManager.reload();
	}
	
	public static void updateItemInHand(Player p) {
		ItemStack hand = p.getItemInHand();
		if (hand.getType() == Material.AIR) return;
		ItemStructure.refresh(hand);
		p.updateInventory();
	}
	
	public static void sendEnchants(Player p) {
		ItemStack hand = p.getItemInHand();
		for (Entry<Enchantment, Integer> e : hand.getEnchantments().entrySet()) {
			p.sendMessage("E: §c"+e.getKey().getName()+" §fP: §a"+e.getValue());
		}
	}
	
	// События боя.
	
	// Регенерация
	public static void regainHealth(EntityRegainHealthEvent event, TurboPlayer victim, double regen) {
		try {
			
			if (victim.enchants.containsKey(TokenEnchant.REGENERATION))
				regen = TokenEnchant.REGENERATION.apply(regen, victim.enchants.get(TokenEnchant.REGENERATION));
			
			event.setAmount(regen);
			
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (playerKillsPlayer) Ошибка в приминении эффекта пользовательских чар. Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
	
	public static void playerKillsPlayer(EntityDeathEvent event, TurboPlayer killer, TurboPlayer victim) {
		try {
			
			if (killer.talismans.containsKey(TokenTalisman.CANNIBALISM))
				TokenTalisman.CANNIBALISM.apply(killer.p, killer.talismans.get(TokenTalisman.CANNIBALISM));
			
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (playerKillsPlayer) Ошибка в приминении эффекта пользовательских чар. Damager: ", killer.p, ", Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
	
	
	public static void mobKillsPlayer(EntityDeathEvent event, LivingEntity killer, TurboPlayer victim) {
		try {
			
			
			
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (mobKillsPlayer) Ошибка в приминении эффекта пользовательских чар. Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
	
	public static void fallDamageToPlayer(EntityDamageEvent event, TurboPlayer victim, double damage) {
		try {
			
			
			
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (fallDamageToPlayer) Ошибка в приминении эффекта пользовательских чар. Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
	
	public static void explosionDamageToPlayer(EntityDamageEvent event, TurboPlayer victim, double damage) {

		try {
			
			
			
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (explosionDamageToPlayer) Ошибка в приминении эффекта пользовательских чар. Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
	
	public static void unknownDamageToPlayer(EntityDamageEvent event, TurboPlayer victim, double damage) {
		
		try {
			if (victim.enchants.containsKey(TokenEnchant.DODGE) && TokenEnchant.DODGE.apply(victim.enchants.get(TokenEnchant.DODGE))) {
				event.setCancelled(true);
				return;
			}
			
			event.setDamage(damage);
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (unknownDamageToPlayer) Ошибка в приминении эффекта пользовательских чар. Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
	
	// Урон с руки игроку. PvP
	public static void playerHitPlayer(PlayerHitPlayerEvent event, TurboPlayer killer, TurboPlayer victim, double damage) {
		try {
			if (victim.enchants.containsKey(TokenEnchant.DODGE) && TokenEnchant.DODGE.apply(victim.enchants.get(TokenEnchant.DODGE))) {
				event.setCancelled(true);
				return;
			}
			
			if (killer.enchants.containsKey(TokenEnchant.RAGE))
				damage = TokenEnchant.RAGE.apply(killer, damage, killer.enchants.get(TokenEnchant.RAGE));
			
			if (killer.enchants.containsKey(TokenEnchant.KICKBACK))
				TokenEnchant.KICKBACK.applyPVP(killer.p, victim.p, killer.enchants.get(TokenEnchant.KICKBACK));
			
			if (killer.enchants.containsKey(TokenEnchant.MADNESS))
				TokenEnchant.MADNESS.apply(killer, victim.p.getBukkitPlayer(), damage, killer.enchants.get(TokenEnchant.MADNESS));
			
			if (killer.enchants.containsKey(TokenEnchant.POSION_THEORY))
				TokenEnchant.POSION_THEORY.apply(killer, victim.p.getBukkitPlayer(), killer.enchants.get(TokenEnchant.POSION_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.SLOWNESS_THEORY))
				TokenEnchant.SLOWNESS_THEORY.apply(killer, victim.p.getBukkitPlayer(), killer.enchants.get(TokenEnchant.SLOWNESS_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.WITHER_THEORY))
				TokenEnchant.WITHER_THEORY.apply(killer, victim.p.getBukkitPlayer(), killer.enchants.get(TokenEnchant.WITHER_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.BLINDNESS_THEORY))
				TokenEnchant.BLINDNESS_THEORY.apply(killer, victim.p.getBukkitPlayer(), killer.enchants.get(TokenEnchant.BLINDNESS_THEORY));
			
			
			if (killer.enchants.containsKey(TokenEnchant.CRIT_CHANCE) && killer.enchants.containsKey(TokenEnchant.CRIT_POWER))
				if (TokenEnchant.CRIT_CHANCE.apply(killer.enchants.get(TokenEnchant.CRIT_CHANCE))) {
					damage = TokenEnchant.CRIT_POWER.apply(damage, killer.enchants.get(TokenEnchant.CRIT_POWER));
				}
			
			
			if (victim.enchants.containsKey(TokenEnchant.SPIKES))
				TokenEnchant.SPIKES.apply(killer, damage, victim.enchants.get(TokenEnchant.SPIKES));
			
			event.setDamage(damage);
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (playerHitPlayer) Ошибка в приминении эффекта пользовательских чар. Damager: ", killer.p, ", Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
	
	// Урон с руки мобу. PvE
	public static void playerHitMob(PlayerHitMobEvent event, TurboPlayer killer, LivingEntity victim, double damage) {
		try {
			
			if (killer.enchants.containsKey(TokenEnchant.MADNESS))
				TokenEnchant.MADNESS.apply(killer, victim, damage, killer.enchants.get(TokenEnchant.MADNESS));
			
			if (killer.enchants.containsKey(TokenEnchant.POSION_THEORY))
				TokenEnchant.POSION_THEORY.apply(killer, victim, killer.enchants.get(TokenEnchant.POSION_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.SLOWNESS_THEORY))
				TokenEnchant.SLOWNESS_THEORY.apply(killer, victim, killer.enchants.get(TokenEnchant.SLOWNESS_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.WITHER_THEORY))
				TokenEnchant.WITHER_THEORY.apply(killer, victim, killer.enchants.get(TokenEnchant.WITHER_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.CRIT_CHANCE) && killer.enchants.containsKey(TokenEnchant.CRIT_POWER))
				if (TokenEnchant.CRIT_CHANCE.apply(killer.enchants.get(TokenEnchant.CRIT_CHANCE)))
					damage = TokenEnchant.CRIT_POWER.apply(damage, killer.enchants.get(TokenEnchant.CRIT_POWER));
			
			event.setDamage(damage);
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (playerHitMob) Ошибка в приминении эффекта пользовательских чар. Damager: ", killer.p, ".");
			e.printStackTrace();
		}
	}
	
	// Урон с лука игроку. PvP
	public static void playerShotPlayer(PlayerShotPlayerEvent event, Projectile projectile, TurboPlayer killer, TurboPlayer victim, double damage) {
		try {
			if (victim.enchants.containsKey(TokenEnchant.DODGE) && TokenEnchant.DODGE.apply(victim.enchants.get(TokenEnchant.DODGE))) {
				event.setCancelled(true);
				return;
			}
			
			
			if (killer.enchants.containsKey(TokenEnchant.CRIT_CHANCE) && killer.enchants.containsKey(TokenEnchant.CRIT_POWER)) {
				if (TokenEnchant.CRIT_CHANCE.apply(killer.enchants.get(TokenEnchant.CRIT_CHANCE))) {
					
					damage = TokenEnchant.CRIT_POWER.apply(damage, killer.enchants.get(TokenEnchant.CRIT_POWER));
					
				}
			}
			
			
			if (killer.enchants.containsKey(TokenEnchant.POSION_THEORY))
				TokenEnchant.POSION_THEORY.apply(killer, victim.p.getBukkitPlayer(), killer.enchants.get(TokenEnchant.POSION_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.SLOWNESS_THEORY))
				TokenEnchant.SLOWNESS_THEORY.apply(killer, victim.p.getBukkitPlayer(), killer.enchants.get(TokenEnchant.SLOWNESS_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.WITHER_THEORY))
				TokenEnchant.WITHER_THEORY.apply(killer, victim.p.getBukkitPlayer(), killer.enchants.get(TokenEnchant.WITHER_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.BLINDNESS_THEORY))
				TokenEnchant.BLINDNESS_THEORY.apply(killer, victim.p.getBukkitPlayer(), killer.enchants.get(TokenEnchant.BLINDNESS_THEORY));
			
			
			event.setDamage(damage);
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (playerShotPlayer) Ошибка в приминении эффекта пользовательских чар. Damager: ", killer.p, ", Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}

	// Урон с лука мобу. PvE
	public static void playerShotMob(PlayerShotMobEvent event, Projectile projectile, TurboPlayer killer, LivingEntity victim, double damage) {
		
		try {
			
			if (killer.enchants.containsKey(TokenEnchant.CRIT_CHANCE) && killer.enchants.containsKey(TokenEnchant.CRIT_POWER))
				if (TokenEnchant.CRIT_CHANCE.apply(killer.enchants.get(TokenEnchant.CRIT_CHANCE)))
					damage = TokenEnchant.CRIT_POWER.apply(damage, killer.enchants.get(TokenEnchant.CRIT_POWER));
			
			
			if (killer.enchants.containsKey(TokenEnchant.POSION_THEORY))
				TokenEnchant.POSION_THEORY.apply(killer, victim, killer.enchants.get(TokenEnchant.POSION_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.SLOWNESS_THEORY))
				TokenEnchant.SLOWNESS_THEORY.apply(killer, victim, killer.enchants.get(TokenEnchant.SLOWNESS_THEORY));
			
			if (killer.enchants.containsKey(TokenEnchant.WITHER_THEORY))
				TokenEnchant.WITHER_THEORY.apply(killer, victim, killer.enchants.get(TokenEnchant.WITHER_THEORY));
			
			event.setDamage(damage);
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (playerShotMob) Ошибка в приминении эффекта пользовательских чар. Damager: ", killer.p, ".");
			e.printStackTrace();
		}
	}
	
	// Применение защиты на ближний урон от моба. EvP
	public static void mobHitPlayer(MobHitPlayerEvent event, LivingEntity killer, TurboPlayer victim, double damage) {

		try {
			if (victim.enchants.containsKey(TokenEnchant.DODGE) && TokenEnchant.DODGE.apply(victim.enchants.get(TokenEnchant.DODGE))) {
				event.setCancelled(true);
				return;
			}
			
			if (victim.enchants.containsKey(TokenEnchant.SPIKES))
				TokenEnchant.SPIKES.apply(killer, damage, victim.enchants.get(TokenEnchant.SPIKES));
			
			event.setDamage(damage);
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (mobHitPlayer) Ошибка в приминении эффекта пользовательских чар. Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
	
	// Применение защиты на дальний урон от моба. EvP
	public static void mobShotPlayer(MobShotPlayerEvent event, LivingEntity killer, TurboPlayer victim, double damage) {
		try {
			if (victim.enchants.containsKey(TokenEnchant.DODGE) && TokenEnchant.DODGE.apply(victim.enchants.get(TokenEnchant.DODGE))) {
				event.setCancelled(true);
				return;
			}
			
			event.setDamage(damage);
		} catch (NullPointerException e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, TokenEngine.class.getSimpleName(), " | (mobShotPlayer) Ошибка в приминении эффекта пользовательских чар. Victim: ", victim.p, ".");
			e.printStackTrace();
		}
	}
}

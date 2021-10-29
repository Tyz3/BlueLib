package ru.kronos.bluelib.api.engine;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.Setting;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.customevent.PlayerHitMobEvent;
import ru.kronos.bluelib.module.damagecontrol.DamageControlTask;

import java.util.*;

public final class DamageControlEngine extends BlueLibEngine implements Listener {

	private static DamageControlEngine inst;

	private DamageControlEngine() {}

	public static DamageControlEngine getInstance() {
		return inst == null ? inst = new DamageControlEngine() : inst;
	}

	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);

		ConfigurationSection c = ConfigEngine.getMainConfig().getConfigurationSection("DamageControlEngine");

		for (String k : c.getStringList("stackDamageCause")) {
			DamageCause cause = DamageCause.valueOf(k.toUpperCase());

			stackDamageCause.add(cause);
		}

		for (String k : c.getStringList("noStackDamageCause")) {
			DamageCause cause = DamageCause.valueOf(k.toUpperCase());

			noStackDamageCause.add(cause);
		}

		DamageControlTask.getInstance().enable();

		setEnabled(true);
	}

	@Override
	public void disable() {
		EntityDamageEvent.getHandlerList().unregister(this);
		PlayerHitMobEvent.getHandlerList().unregister(this);
		EntityDeathEvent.getHandlerList().unregister(this);

		DamageControlTask.getInstance().disable();

		stackDamageCause.clear();
		noStackDamageCause.clear();

		setEnabled(false);
	}

	private final List<DamageCause> noStackDamageCause = new ArrayList<>();
	private final List<DamageCause> stackDamageCause = new ArrayList<>();
	
	private final Map<DamageCause, Long> timings = new HashMap<>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onPlayerDamage(EntityDamageEvent e) {
		if (e.getEntityType() == EntityType.PLAYER) {

			if (Setting.DamageControlEngine_enableTimings.getBool()) {
				long t = timings.getOrDefault(e.getCause(), System.currentTimeMillis());
				long t2 = System.currentTimeMillis();

				LogEngine.debugMsg(LoggingLevel.MINIMUM, e.getEntity(), " >> ", e.getCause(), " dmg:", e.getDamage(), " t:", t2-t, "мс");
				timings.put(e.getCause(), t2);
			}
			
			BlueLibPlayer player = OnlineEngine.getPlayer(e.getEntity().getUniqueId());
			DamageCause cause = e.getCause();
			
			if (cause == DamageCause.CUSTOM) return;

			assert player != null;
			Map<DamageCause, Double> deferredDamage = player.getDeferredDamage();

			e.setCancelled(true);
			
			if (stackDamageCause.contains(cause)) {
				deferredDamage.put(cause, e.getDamage() + deferredDamage.getOrDefault(cause, 0D));
				
			} else if (noStackDamageCause.contains(cause)) {
				deferredDamage.put(cause, deferredDamage.getOrDefault(cause, e.getDamage()));

			} else {
				e.setCancelled(false);
			}
			
		}
	}

	private static final Map<UUID, Long> hitCooldown = new HashMap<>();
	private static final Map<UUID, Integer> hitCounter = new HashMap<>();
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onPlayerHitMob(PlayerHitMobEvent e) {
		if (Setting.DamageControlEngine_HealthOverDamage_enable.getBool()) {
			long now = System.currentTimeMillis();
			UUID uuid = e.getVictim().getUniqueId();
			
			long before = hitCooldown.getOrDefault(uuid, 0L);
			int counter = hitCounter.getOrDefault(uuid, Setting.DamageControlEngine_HealthOverDamage_hitCounter.getInt());
			
			if (before == 0L) {
				hitCooldown.put(uuid, now);
				hitCounter.put(uuid, counter);
				return;
			}
			
			if (now - before < Setting.DamageControlEngine_HealthOverDamage_hitCooldownMillis.getLong()) {
				if (counter > 0) {
					double newHealth = e.getVictim().getHealth() - e.getDamage();
					
					
					e.setCancelled(true);
					e.getVictim().setHealth(Math.max(newHealth, 0D));

					if (API3rdPartyEngine.enabledCombatLog()) {
						// TODO
//						CombatLogAPI.claimCombat(OnlineManager.getPlayer(e.getDamager().getUniqueId()), e.getVictim());
					}
					
					hitCounter.put(uuid, counter - 1);
					System.out.println("Урон (без анимации): "+e.getDamage()+", HP: "+e.getVictim().getHealth());
				} else {
					
					hitCounter.put(uuid, Setting.DamageControlEngine_HealthOverDamage_hitCounter.getInt());
					System.out.println("Урон: "+e.getDamage()+", HP: "+e.getVictim().getHealth());
				}
			} else {
				System.out.println("Урон: "+e.getDamage()+", HP: "+e.getVictim().getHealth());
			}
			
			hitCooldown.put(uuid, now);

		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onMobDeathEvent(EntityDeathEvent e) {
		hitCooldown.remove(e.getEntity().getUniqueId());
		hitCounter.remove(e.getEntity().getUniqueId());
	}

}

package ru.kronos.bluelib.api.engine;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.module.customevent.*;

public final class EventEngine extends BlueLibEngine implements Listener {

	private static EventEngine inst;

	private EventEngine() {}

	public static EventEngine getInstance() {
		return inst == null ? inst = new EventEngine() : inst;
	}
	
	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);
		setEnabled(true);
	}
	
	@Override
	public void disable() {
		EntityDamageByEntityEvent.getHandlerList().unregister(this);
		setEnabled(false);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
		callEntityDamageByEntity(e);
	}
	
	private static void callEntityDamageByEntity(EntityDamageByEntityEvent e) {
		// Атака с руки.
		if (e.getCause() == DamageCause.ENTITY_ATTACK) {
			
			if (e.getDamager() instanceof Player) {
				Player damager = (Player) e.getDamager();
				
				if (e.getEntity() instanceof Player) {
					Player victim = (Player) e.getEntity();
					
					// melee PVP
					PlayerHitPlayerEvent ev = new PlayerHitPlayerEvent(damager, victim, e.getCause(), e.getDamage());
					Bukkit.getPluginManager().callEvent(ev);
					e.setCancelled(ev.isCancelled());
					e.setDamage(ev.getDamage());
				} else {
					LivingEntity victim = (LivingEntity) e.getEntity();
					
					// melee PVE
					PlayerHitMobEvent ev = new PlayerHitMobEvent(damager, victim, e.getCause(), e.getDamage());
					Bukkit.getPluginManager().callEvent(ev);
					e.setCancelled(ev.isCancelled());
					e.setDamage(ev.getDamage());
				}
				
			} else {
				LivingEntity damager = (LivingEntity) e.getDamager();
				
				if (e.getEntity() instanceof Player) {
					Player victim = (Player) e.getEntity();
					
					// melee EVP
					MobHitPlayerEvent ev = new MobHitPlayerEvent(damager, victim, e.getCause(), e.getDamage());
					Bukkit.getPluginManager().callEvent(ev);
					e.setCancelled(ev.isCancelled());
					e.setDamage(ev.getDamage());
				} else {
					LivingEntity victim = (LivingEntity) e.getEntity();
					
					// melee EVE
					MobHitMobEvent ev = new MobHitMobEvent(damager, victim, e.getCause(), e.getDamage());
					Bukkit.getPluginManager().callEvent(ev);
					e.setCancelled(ev.isCancelled());
					e.setDamage(ev.getDamage());
				}
				
			}
			
			
		// Получен урон от снаряда.
		} else if (e.getCause() == DamageCause.PROJECTILE) {
			Projectile projectile = (Projectile) e.getDamager();
			ProjectileSource projectileSource = projectile.getShooter();
			
			if (projectileSource instanceof Player) {
				Player damager = (Player) projectileSource;
				
				if (e.getEntity() instanceof Player) {
					Player victim = (Player) e.getEntity();
					
					// range PVP
					PlayerShotPlayerEvent ev = new PlayerShotPlayerEvent(damager, victim, e.getCause(), e.getDamage(), projectile);
					Bukkit.getPluginManager().callEvent(ev);
					e.setCancelled(ev.isCancelled());
					e.setDamage(ev.getDamage());
				} else {
					LivingEntity victim = (LivingEntity) e.getEntity();
					
					// range PVE
					PlayerShotMobEvent ev = new PlayerShotMobEvent(damager, victim, e.getCause(), e.getDamage(), projectile);
					Bukkit.getPluginManager().callEvent(ev);
					e.setCancelled(ev.isCancelled());
					e.setDamage(ev.getDamage());
				}
				
			} else {

				// Урон получил игрок.
				if (e.getEntity() instanceof Player) {
					Player victim = (Player) e.getEntity();
					
					// range EVP
					MobShotPlayerEvent ev = new MobShotPlayerEvent(projectileSource, victim, e.getCause(), e.getDamage(), projectile);
					Bukkit.getPluginManager().callEvent(ev);
					e.setCancelled(ev.isCancelled());
					e.setDamage(ev.getDamage());
				} else {
					LivingEntity victim = (LivingEntity) e.getEntity();
					
					// range EVE
					MobShotMobEvent ev = new MobShotMobEvent(projectileSource, victim, e.getCause(), e.getDamage(), projectile);
					Bukkit.getPluginManager().callEvent(ev);
					e.setCancelled(ev.isCancelled());
					e.setDamage(ev.getDamage());
				}
			}
		
		}
	}
}

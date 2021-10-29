package ru.kronos.bluelib.module.customevent;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;

public class MobShotPlayerEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private boolean cancel;
	private double damage;
	private final ProjectileSource damager;
	private final Player victim;
	private final DamageCause cause;
	private final Projectile projectile;

	public MobShotPlayerEvent(ProjectileSource damager, Player victim, DamageCause cause, double damage, Projectile projectile) {
		this.damage = damage;
		this.damager = damager;
		this.victim = victim;
		this.cause = cause;
		this.projectile = projectile;
	}
	
	@Override
	public boolean isCancelled() {
		return cancel;
	}
	
	@Override
	public void setCancelled(boolean arg0) {
		cancel = arg0;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public ProjectileSource getDamager() {
		return damager;
	}

	public Player getVictim() {
		return victim;
	}

	public DamageCause getCause() {
		return cause;
	}

	public Projectile getProjectile() {
		return projectile;
	}
}

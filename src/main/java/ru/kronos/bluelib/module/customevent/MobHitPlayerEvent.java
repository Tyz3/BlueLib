package ru.kronos.bluelib.module.customevent;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class MobHitPlayerEvent extends Event implements Cancellable {
	
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
	private final LivingEntity damager;
	private final Player victim;
	private final DamageCause cause;
	
	public MobHitPlayerEvent(LivingEntity damager, Player victim, DamageCause cause, double damage) {
		this.damage = damage;
		this.damager = damager;
		this.victim = victim;
		this.cause = cause;
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

	public LivingEntity getDamager() {
		return damager;
	}

	public Player getVictim() {
		return victim;
	}

	public DamageCause getCause() {
		return cause;
	}
}

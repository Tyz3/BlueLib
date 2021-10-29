package ru.kronos.bluelib.module.customevent;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class PlayerHitMobEvent extends Event implements Cancellable {
	
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
	private final Player damager;
	private final LivingEntity victim;
	private final DamageCause cause;
	
	public PlayerHitMobEvent(Player damager, LivingEntity victim, DamageCause cause, double damage) {
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
		this.cancel = arg0;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public Player getDamager() {
		return damager;
	}

	public LivingEntity getVictim() {
		return victim;
	}

	public DamageCause getCause() {
		return cause;
	}

}

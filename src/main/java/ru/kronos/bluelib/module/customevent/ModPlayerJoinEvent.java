package ru.kronos.bluelib.module.customevent;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

public class ModPlayerJoinEvent extends Event {
	
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private final PlayerJoinEvent event;
	private final BlueLibPlayer player;
	
	public ModPlayerJoinEvent(PlayerJoinEvent event) {
		this.event = event;
		this.player = OnlineEngine.getPlayer(event.getPlayer().getUniqueId());
	}
	
	public ModPlayerJoinEvent(PlayerJoinEvent event, BlueLibPlayer player) {
		this.event = event;
		this.player = player;
	}
	
	public String getJoinMessage() {
		return event.getJoinMessage();
	}
	
	public BlueLibPlayer getPlayer() {
		return player;
	}

}

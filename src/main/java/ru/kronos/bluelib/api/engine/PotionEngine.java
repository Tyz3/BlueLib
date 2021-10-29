package ru.kronos.bluelib.api.engine;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.template.BlueLibEngine;

public final class PotionEngine extends BlueLibEngine implements Listener {

	private static PotionEngine inst;

	private PotionEngine() {}

	public static PotionEngine getInstance() {
		return inst == null ? inst = new PotionEngine() : inst;
	}
	
	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);
		setEnabled(true);
	}

	@Override
	public void disable() {
		PlayerItemConsumeEvent.getHandlerList().unregister(this);
		setEnabled(false);
	}

	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private static void onPotionConsume(PlayerItemConsumeEvent e) {
		
		if (e.getItem().getType() == Material.POTION) {
			
//			PotionMeta meta = (PotionMeta) e.getItem();
			
		}
	}
	
	
}

package ru.kronos.bluelib.api.engine;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.chestmenu.Container;

import java.util.Map.Entry;
import java.util.UUID;

public final class ChestMenuEngine extends BlueLibEngine implements Listener {

	private static ChestMenuEngine inst;

	private ChestMenuEngine() {}

	public static ChestMenuEngine getInstance() {
		return inst == null ? inst = new ChestMenuEngine() : inst;
	}
	
	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);

		for (Entry<UUID, BlueLibPlayer> p : OnlineEngine.getOnline().entrySet()) {
			if (p.getValue().hasCurrentContainer())
				p.getValue().closeInventory();
			// currentContainer удалится по вызову события InventoryCloseEvent.
		}
	}
	
	@Override
	public void disable() {
		InventoryClickEvent.getHandlerList().unregister(this);
		InventoryCloseEvent.getHandlerList().unregister(this);

		OnlineEngine.getOnline().values().forEach(BlueLibPlayer::closeInventory);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	private void onContainerClick(InventoryClickEvent e) {
		if (e.getClickedInventory() == null) return;
		if (!(e.getWhoClicked() instanceof Player)) return;
		
		BlueLibPlayer p = OnlineEngine.getPlayer(e.getWhoClicked().getUniqueId());


		assert p != null;
		if (!p.hasCurrentContainer()) return;
		
		
		Container c = p.getCurrentContainer();
		
		if (e.getClickedInventory() == p.getOpenInventory().getBottomInventory()) {
			if (!c.isFrozenBottomInventory()) {
				switch (e.getClick()) {
				case DOUBLE_CLICK:
					if (c.isFrozenDoubleBottomClick())
						e.setCancelled(true);
					break;
				case SHIFT_LEFT:
				case SHIFT_RIGHT:
					if (c.isFrozenShiftBottomClick())
						e.setCancelled(true);
					break;
				default: break;
				}
			} else {
				// TODO
			}
		} else {
			e.setCancelled(c.isFrozen(e.getRawSlot()));
			c.makeAction(p, e.getRawSlot(), e.getClick());
		}
		
		
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onContainerClose(InventoryCloseEvent e) {
		BlueLibPlayer p = OnlineEngine.getPlayer(e.getPlayer());
		if (p != null && p.hasCurrentContainer()) {
			p.setCurrentContainer(null);
		}
	}
}

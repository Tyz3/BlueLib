package ru.kronos.bluelib.module.chestmenu;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.BlueItemStack;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.chestmenu.item.ChestElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Container {
	
	private final Inventory inv;
	private final Map<Integer, ChestElement> elements = new HashMap<>();
	private final int size;
	private final String title;
	
	private boolean fullyFrozen = false;
	private final boolean[] frozenSlots;
	private boolean frozenBottomInventory = false;
	private boolean frozenDoubleBottomClick = false;
	private boolean frozenShiftBottomClick = false;
	
	public Container(InventoryHolder owner, int size, String title) {
		this.inv = Bukkit.createInventory(owner, (int) Math.ceil((float) size / 9) * 9, title);
		this.size = inv.getSize();
		this.title = title;
		this.frozenSlots = new boolean[this.size];
	}
	
	public Container(InventoryHolder owner, InventoryType type, String title) {
		this.inv = Bukkit.createInventory(owner, type, title.replace("&", "§"));
		this.size = inv.getSize();
		this.title = title;
		this.frozenSlots = new boolean[this.size];
	}
	
	public Container(int size, String title) {
		this(null, size, title);
	}
	
	public Container(InventoryType type, String title) {
		this(null, type, title);
	}
	
	/**
	 * Вернёт true, если слот заморожен.
	 * Флаг fullyFrozen имеет больший приоритет.
	 * @return fullyFrozen ? true : frozenSlots[rawSlot]
	 */
	public Container makeAction(BlueLibPlayer player, int rawSlot, ClickType clickType) {
		if (elements.containsKey(rawSlot))
			elements.get(rawSlot).action(player, clickType);
		return this;
	}
	
	public Container putElement(int rawSlot, ChestElement chestElement) {
		elements.put(rawSlot, chestElement);
		inv.setItem(rawSlot, chestElement);
		return this;
	}
	
	public Container clearElement(int rawSlot) {
		if (elements.remove(rawSlot) == null)
			LogEngine.debugMsg(LoggingLevel.WARNING, Container.class.getSimpleName(), " | Элемент под номером ", rawSlot, " не был удален, ввиду его отсутствия.");
		else inv.clear(rawSlot);
		return this;
	}
	
	public Container clearAllElements() {
		int fill = getFill();
		elements.clear();
		inv.clear();
		LogEngine.debugMsg(LoggingLevel.DEBUG, Container.class.getSimpleName(), " | Удалёно ", fill, " элементов из меню '", title, "'.");
		return this;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getFill() {
		return elements.size();
	}
	
	public void openFor(BlueLibPlayer player) {
		player.openInventory(inv);
		player.setCurrentContainer(this);
	}
	
	/**
	 * Если число в rawSlots отрицательное, то слот под этим номером будет разморожен,
	 * иначе - заморожен.
	 * Если все слоты заморожены, на контейнер ставится флаг fullyFrozen.
	 * @param rawSlots - номера слотов, которые нужно заморозить или разморозить.
	 */
	public Container freezeSlot(boolean freeze, int... rawSlots) {
		int badCount = 0;
		for (int rawSlot : rawSlots) {
			if (rawSlot < 0 || size < rawSlot) {
				LogEngine.debugMsg(LoggingLevel.WARNING, Container.class.getSimpleName(), " | Не получилось (за/раз)морозить слот ", rawSlot, ", поскольку он выходит за общий размер ", size, ".");
				badCount++;
				continue;
			}
			frozenSlots[rawSlot] = freeze;
		}
		fullyFrozen = rawSlots.length - badCount == size;
		return this;
	}
	
	/**
	 * Замораживает все слоты контейнера.
	 */
	public Container setFullyFrozen(boolean value) {
		Arrays.fill(frozenSlots, value);
		fullyFrozen = value;
		return this;
	}
	
	public boolean isFrozen(int rawSlot) {
		return fullyFrozen || frozenSlots[rawSlot];
	}
	
	public Container setFrozenBottomInventory(boolean value) {
		frozenBottomInventory = value;
		return this;
	}
	
	public boolean isFrozenBottomInventory() {
		return frozenBottomInventory;
	}
	
	public Container setFrozenDoubleBottomClick(boolean value) {
		frozenDoubleBottomClick = value;
		return this;
	}
	
	public boolean isFrozenDoubleBottomClick() {
		return frozenDoubleBottomClick;
	}
	
	public Container setFrozenShiftBottomClick(boolean value) {
		frozenShiftBottomClick = value;
		return this;
	}
	
	public boolean isFrozenShiftBottomClick() {
		return frozenShiftBottomClick;
	}
	
	/**
	 * Позволяет изменить существующий предмет в контейнере.
	 */
	public Container changeItem(int rawSlot, BlueItemStack newItem) {
		if (elements.containsKey(rawSlot)) {
			inv.setItem(
					rawSlot,
					elements.get(rawSlot).makeItemAs(newItem)
			);
		}
		return this;
	}
}

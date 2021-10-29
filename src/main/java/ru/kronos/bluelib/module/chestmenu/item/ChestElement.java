package ru.kronos.bluelib.module.chestmenu.item;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.BlueItemStack;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.chestmenu.item.action.ChestElementAction;

import java.util.EnumMap;
import java.util.Map;

public abstract class ChestElement extends BlueItemStack {
	
	private Map<ClickType, ChestElementAction> actions = new EnumMap<>(ClickType.class);

	public ChestElement() {}

	public ChestElement(Material type) {
		super(type);
	}

	public ChestElement(Material type, int amount) {
		super(type, amount);
	}

	public ChestElement(BlueItemStack stack) {
		super(stack);
	}

	public ChestElement(Map<ClickType, ChestElementAction> actions) {
		this.actions = actions;
	}

	public ChestElement(Material type, Map<ClickType, ChestElementAction> actions) {
		super(type);
		this.actions = actions;
	}

	public ChestElement(Material type, int amount, Map<ClickType, ChestElementAction> actions) {
		super(type, amount);
		this.actions = actions;
	}

	public ChestElement(BlueItemStack stack, Map<ClickType, ChestElementAction> actions) {
		super(stack);
		this.actions = actions;
	}
	
	public void action(BlueLibPlayer player, ClickType clickType) {
		if (actions.containsKey(clickType))
			actions.get(clickType).perform(player);
	}
	
	public ChestElement putAction(ClickType clickType, ChestElementAction action) {
		if (actions.containsKey(clickType)) {
			LogEngine.debugMsg(LoggingLevel.WARNING, ChestElement.class.getSimpleName(), " | Действие на клик ", clickType, " уже было записано, произошла перезапись.");
		}
		actions.put(clickType, action);
		return this;
	}
	
	public ChestElement removeAction(ClickType clickType) {
		if (!actions.containsKey(clickType)) {
			LogEngine.debugMsg(LoggingLevel.WARNING, ChestElement.class.getSimpleName(), " | Действие ", clickType, " не было удалено ввиду его отсутствия.");
		} else {
			actions.remove(clickType);
		}
		return this;
	}
	
	public ChestElement clearActions() {
		actions.clear();
		return this;
	}
}

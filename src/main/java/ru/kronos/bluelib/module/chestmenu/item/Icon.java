package ru.kronos.bluelib.module.chestmenu.item;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import ru.kronos.bluelib.api.template.BlueItemStack;
import ru.kronos.bluelib.module.chestmenu.item.action.ChestElementAction;

import java.util.Map;

public class Icon extends ChestElement {

	public Icon() {}

	public Icon(Material type) {
		super(type);
	}

	public Icon(Material type, int amount) {
		super(type, amount);
	}

	public Icon(BlueItemStack stack) {
		super(stack);
	}

	public Icon(Map<ClickType, ChestElementAction> actions) {
		super(actions);
	}

	public Icon(Material type, Map<ClickType, ChestElementAction> actions) {
		super(type, actions);
	}

	public Icon(Material type, int amount, Map<ClickType, ChestElementAction> actions) {
		super(type, amount, actions);
	}

	public Icon(BlueItemStack stack, Map<ClickType, ChestElementAction> actions) {
		super(stack, actions);
	}
}
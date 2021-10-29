package ru.kronos.bluelib.module.chestmenu.item.action;

import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

public class ChestActionClose implements ChestElementAction {
	
	@Override
	public void perform(BlueLibPlayer player) {
		player.closeInventory();
	}

}

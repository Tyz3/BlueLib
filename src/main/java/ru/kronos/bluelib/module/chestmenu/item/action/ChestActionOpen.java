package ru.kronos.bluelib.module.chestmenu.item.action;

import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.chestmenu.Container;

public class ChestActionOpen implements ChestElementAction {
	
	private final Container forOpen;
	
	public ChestActionOpen(Container forOpen) {
		this.forOpen = forOpen;
	}
	
	@Override
	public void perform(BlueLibPlayer player) {
		forOpen.openFor(player);
	}

}

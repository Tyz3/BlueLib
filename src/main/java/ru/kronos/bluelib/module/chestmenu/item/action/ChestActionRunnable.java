package ru.kronos.bluelib.module.chestmenu.item.action;

import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

public class ChestActionRunnable implements ChestElementAction {
	
	private final Runnable runnable;
	
	public ChestActionRunnable(Runnable runnable) {
		this.runnable = runnable;
	}
	
	@Override
	public void perform(BlueLibPlayer player) {
		runnable.run();
	}

}

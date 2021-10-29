package ru.kronos.bluelib.module.chestmenu.item.action;

import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.Placeholder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChestActionConsole implements ChestElementAction {

	private final List<String> cmds;
	private final boolean closeChestBeforeUse;

	public ChestActionConsole(boolean closeChestBeforeUse, List<String> cmds) {
		this.closeChestBeforeUse = closeChestBeforeUse;
		this.cmds = cmds == null ? new ArrayList<>() : cmds;
	}

	public ChestActionConsole(boolean closeChestBeforeUse, String... cmds) {
		this(closeChestBeforeUse, Arrays.asList(cmds));
	}
	
	@Override
	public void perform(BlueLibPlayer player) {
		if (closeChestBeforeUse) player.closeInventory();
		OnlineEngine.dispatchCommand(Placeholder.set(cmds, "{player}", player.getName()));
	}
	
}

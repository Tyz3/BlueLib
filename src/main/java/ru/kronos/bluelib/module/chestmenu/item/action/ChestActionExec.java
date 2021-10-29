package ru.kronos.bluelib.module.chestmenu.item.action;

import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChestActionExec implements ChestElementAction {
	
	private final List<String> cmds;
	private final boolean closeChestBeforeUse;
	
	public ChestActionExec(boolean closeChestBeforeUse, List<String> cmds) {
		this.closeChestBeforeUse = closeChestBeforeUse;
		this.cmds = cmds == null ? new ArrayList<>() : cmds;
	}
	
	public ChestActionExec(boolean closeChestBeforeUse, String... cmds) {
		this(closeChestBeforeUse, Arrays.asList(cmds));
	}
	
	@Override
	public void perform(BlueLibPlayer player) {
		if (closeChestBeforeUse) player.closeInventory();
		cmds.forEach(player.getBukkitPlayer()::performCommand);
//		OnlineEngine.dispatchCommand(Placeholder.set(cmds, "{player}", player.getName()));
	}
	
}

package ru.kronos.bluelib.module.chestmenu.item.action;

import org.bukkit.inventory.ItemStack;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

public class ChestActionGiveItem implements ChestElementAction {

    private final ItemStack item;

    public ChestActionGiveItem(ItemStack item) {
        this.item = item;
    }

    @Override
    public void perform(BlueLibPlayer player) {
        player.giveItem(item);
    }
}

package ru.kronos.chestcommands;

import org.bukkit.event.inventory.ClickType;
import ru.kronos.bluelib.api.template.BlueItemStack;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.chestmenu.Container;
import ru.kronos.bluelib.module.chestmenu.item.Icon;
import ru.kronos.bluelib.module.chestmenu.item.action.ChestActionGiveItem;
import ru.kronos.bluelib.module.chestmenu.item.action.ChestActionExec;

import java.util.ArrayList;
import java.util.List;

public class ItemMenu {

    public List<Container> pages = new ArrayList<>();

    public ItemMenu(String cmd, List<BlueItemStack> items) {
        int pageAmount = (int) Math.ceil(items.size() / 45D);

        for (int i = 0; i < pageAmount; i++) {
            Container c = new Container(54, "Страница " + (i + 1));

            int j = 0;
            do {
                BlueItemStack stack = items.remove(0);

                Icon icon = new Icon(stack);
                icon.putAction(ClickType.LEFT, new ChestActionGiveItem(stack));
                c.putElement(j, icon);

                j++;
            } while (j % 45 != 0 && !items.isEmpty());

            Icon bef = new Icon(CCEngine.beforeButton);
            if (1 <= i) {
                bef.putAction(ClickType.LEFT, new ChestActionExec(false, cmd + " " + i ));
            }

            Icon nxt = new Icon(CCEngine.nextButton);
            if (i + 1 < pageAmount) {
                nxt.putAction(ClickType.LEFT, new ChestActionExec(false, cmd + " " + (i + 2) ));
            }

            c.putElement(45, bef);
            c.putElement(53, nxt);

            c.setFullyFrozen(true);
            c.setFrozenDoubleBottomClick(true);
            c.setFrozenShiftBottomClick(true);

            pages.add(c);
        }
    }

    public void openPage(BlueLibPlayer player, int page) {
        if (0 < page && page <= pages.size()) {
            pages.get(page - 1).openFor(player);
        }
    }

}

package ru.kronos.chestcommands;

import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.BlueItemStack;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.extra.LoggingLevel;

import java.util.ArrayList;
import java.util.List;

public class CCEngine extends BlueLibEngine {

    private static CCEngine inst;

    private CCEngine() {}

    public static CCEngine getInstance() {
        return inst == null ? inst = new CCEngine() : inst;
    }

    private static final List<BlueItemStack> items = new ArrayList<>();
    private static final List<BlueItemStack> decos = new ArrayList<>();

    public static BlueItemStack beforeButton;
    public static BlueItemStack nextButton;

    private static ItemMenu itemDB;
    private static ItemMenu decoDB;

    @Override
    public void enable() {

        beforeButton = BlueItemStack.parseSimple(
                Main.config.get().getString("beforeButton", "yellow_stained_glass_pane:0:1:&a&l<<<")
        );

        nextButton = BlueItemStack.parseSimple(
                Main.config.get().getString("nextButton", "yellow_stained_glass_pane:0:1:&a&l>>>")
        );

        for (String elem : Main.config.get().getStringList("items")) {
            BlueItemStack item = BlueItemStack.parseSimple(elem);

            if (item == null) {
                LogEngine.debugMsg(LoggingLevel.WARNING, Main.inst.getName(), " | Предмет ", elem, " не найден и был пропущен.");
                continue;
            }

            items.add(item);
        }

        for (String elem : Main.config.get().getStringList("deco")) {
            BlueItemStack item = BlueItemStack.parseSimple(elem);

            if (item == null) {
                LogEngine.debugMsg(LoggingLevel.WARNING, Main.inst.getName(), " | Предмет ", elem, " не найден и был пропущен.");
                continue;
            }

            decos.add(item);
        }

        itemDB = new ItemMenu("itemdatabase", items);
        decoDB = new ItemMenu("decodatabase", decos);

        LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Зарегистрировано ", items.size(), " предмет(-a/-ов) в ItemDatabase.");
        LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Зарегистрировано ", decos.size(), " предмет(-a/-ов) в DecoDatabase.");

    }

    @Override
    public void disable() {
        items.clear();
        decos.clear();
    }

    public ItemMenu getItemDB() {
        return itemDB;
    }

    public ItemMenu getDecoDB() {
        return decoDB;
    }
}

package ru.kronos.chestcommands;

import ru.kronos.bluelib.api.engine.ConfigEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.api.template.config.BukkitConfig;
import ru.kronos.chestcommands.command.DecoDatabaseCMD;
import ru.kronos.chestcommands.command.ItemDatabaseCMD;

public class Main extends BlueLibPlugin {

    public static Main inst;

    public static BukkitConfig config;

    @Override
    public void load() {
        inst = this;
        config = ConfigEngine.registerNewConfig(this, "config.yml");
    }

    @Override
    public void enable() {
        config.reload();
        CCEngine.getInstance().enable();
        ItemDatabaseCMD.newInstance();
        DecoDatabaseCMD.newInstance();
    }

    @Override
    public void disable() {
        CCEngine.getInstance().disable();
    }
}

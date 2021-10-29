package ru.kronos.resourcepacks;

import ru.kronos.bluelib.api.engine.ConfigEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.api.template.config.BukkitConfig;
import ru.kronos.resourcepacks.command.RemoveResourcePackCMD;
import ru.kronos.resourcepacks.command.UpdateResourcePackCMD;

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
        RPEngine.getInstance().enable();
        UpdateResourcePackCMD.newInstance();
        RemoveResourcePackCMD.newInstance();
    }

    @Override
    public void disable() {
        RPEngine.getInstance().disable();
    }
}

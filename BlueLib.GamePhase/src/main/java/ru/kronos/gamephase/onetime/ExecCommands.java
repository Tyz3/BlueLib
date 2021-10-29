package ru.kronos.gamephase.onetime;

import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

public class ExecCommands extends OneTimeAction {

    private final String[] COMMANDS;

    private ExecCommands(ConfigurationSection c) {
        if (c.contains("exec-cmds") && c.isList("exec-cmds")) {
            COMMANDS = c.getStringList("exec-cmds").toArray(String[]::new);
        } else {
            COMMANDS = null;
        }
    }

    @Override
    public void perform(BlueLibPlayer player) {
        OnlineEngine.dispatchCommand(setPlaceholders(player, COMMANDS));
    }

    @Override
    public boolean initialized() {
        return COMMANDS != null;
    }

    public static OneTimeAction createFromConfig(ConfigurationSection c) {
        return new ExecCommands(c);
    }
}

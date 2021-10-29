package ru.kronos.gamephase.regular;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class CommandRule extends RegularRule {

    private final boolean asBlacklist;
    private final List<String> commands;

    private CommandRule(ConfigurationSection c) {
        asBlacklist = c.getBoolean("command-rule.as-blacklist", false);
        commands = c.getStringList("command-rule.list");
    }

    public boolean isCommandAllowed(String command) {
        for (String cmd : commands) {
            if (command.startsWith(cmd, 1)) {
                return !asBlacklist;
            }
        }

        return asBlacklist;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public static RegularRule createFromConfig(ConfigurationSection c) {
        return new CommandRule(c);
    }
}

package ru.kronos.gamephase.onetime;

import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

public class SendMessage extends OneTimeAction {

    private final String[] MESSAGE;

    private SendMessage(ConfigurationSection c) {
        if (c.isString("message")) {
            MESSAGE = new String[] {c.getString("message", "null").replace("&", "§")};
        } else if (c.isList("message")) {
            MESSAGE = c.getStringList("message").stream()
                    .map(s -> s.replace("&", "§")).toArray(String[]::new);
        } else {
            MESSAGE = null;
        }
    }

    @Override
    public void perform(BlueLibPlayer player) {
        player.sendMessages(setPlaceholders(player, MESSAGE));
    }

    @Override
    public boolean initialized() {
        return MESSAGE != null;
    }

    public static OneTimeAction createFromConfig(ConfigurationSection c) {
        return new SendMessage(c);
    }
}

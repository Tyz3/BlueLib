package ru.kronos.gamephase.onetime;

import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

import java.util.List;

public class SendTitle extends OneTimeAction {

    private final String TITLE;
    private final String SUBTITLE;

    private SendTitle(ConfigurationSection c) {

        if (c.isString("title")) {
            TITLE = c.getString("title", "null").replace("&", "§");
            SUBTITLE = "";
        } else if (c.isList("title")) {
            List<String> titles = c.getStringList("title");

            if (titles.isEmpty()) {
                TITLE = SUBTITLE = null;
                return;
            }

            TITLE = titles.get(0).replace("&", "§");

            if (titles.size() > 1) {
                SUBTITLE = titles.get(1).replace("&", "§");
            } else SUBTITLE = "";
        } else {
            TITLE = SUBTITLE = null;
        }
    }

    @Override
    public void perform(BlueLibPlayer player) {
        if (TITLE != null) {
            player.getBukkitPlayer().sendTitle(TITLE, SUBTITLE, 5, 40, 20);
        }
    }

    @Override
    public boolean initialized() {
        return TITLE != null;
    }

    public static OneTimeAction createFromConfig(ConfigurationSection c) {
        return new SendTitle(c);
    }
}

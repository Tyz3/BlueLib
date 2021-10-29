package ru.kronos.gamephase.onetime;

import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

public class ChangeGamemode extends OneTimeAction {

    private final GameMode GAMEMODE;

    private ChangeGamemode(ConfigurationSection c) {
        if (c.contains("gamemode") && c.isString("gamemode")) {
            GAMEMODE = GameMode.valueOf(c.getString("gamemode", "SURVIVAL"));
        } else {
            GAMEMODE = null;
        }
    }

    @Override
    public void perform(BlueLibPlayer player) {
        player.getBukkitPlayer().setGameMode(GAMEMODE);
    }

    @Override
    public boolean initialized() {
        return GAMEMODE != null;
    }

    public static OneTimeAction createFromConfig(ConfigurationSection c) {
        return new ChangeGamemode(c);
    }
}

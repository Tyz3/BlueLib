package ru.kronos.gamephase.onetime;

import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.gamephase.GamePhase;

import java.util.List;
import java.util.stream.Collectors;

public abstract class OneTimeAction {

    public abstract void perform(BlueLibPlayer player);
    public abstract boolean initialized();

    public void registerIfInited(GamePhase gamePhase, String moduleName) {
        if (initialized()) {
            switch (moduleName) {
                case "join":
                    gamePhase.addJoinAction(this);
                    break;
                case "exit":
                    gamePhase.addExitAction(this);
                    break;
            }
        }
    }

    protected List<String> setPlaceholders(BlueLibPlayer player, List<String> strs) {
        return strs.stream().map(s -> setPlaceholders(player, s)).collect(Collectors.toList());
    }

    protected String[] setPlaceholders(BlueLibPlayer player, String... strs) {
        for (int i = 0; i < strs.length; i++) {
            strs[i] = setPlaceholders(player, strs[i]);
        }

        return strs;
    }

    protected String setPlaceholders(BlueLibPlayer player, String str) {
        return str
                .replace("{playerName}", player.getName())
                .replace("{worldName}", player.getWorld().getName())
                .replace("{locX}", String.valueOf(player.getLocation().getX()))
                .replace("{locY}", String.valueOf(player.getLocation().getY()))
                .replace("{locZ}", String.valueOf(player.getLocation().getZ()))
                .replace("{locPitch}", String.valueOf(player.getLocation().getPitch()))
                .replace("{locYaw}", String.valueOf(player.getLocation().getYaw()));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}

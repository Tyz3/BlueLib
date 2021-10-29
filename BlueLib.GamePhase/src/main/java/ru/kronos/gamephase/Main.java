package ru.kronos.gamephase;

import ru.kronos.bluelib.api.engine.ConfigEngine;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.StorageEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.api.template.config.BukkitConfig;
import ru.kronos.bluelib.api.template.storage.JSONPlayerStorage;
import ru.kronos.bluelib.api.template.storage.PlayerStorageTemplate;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.gamephase.command.GamePhaseCMD;

public class Main extends BlueLibPlugin {

    public static Main inst;

    public static BukkitConfig config;

    @Override
    public void load() {
        inst = this;
        config = ConfigEngine.registerNewConfig(this, "config.yml");

        StorageEngine.registerNewPlayerStorageTemplate(new GamePhaseStorage());
    }

    @Override
    public void enable() {
        config.reload();

        GamePhaseEngine.getInstance().enable();
        RegularTask.getInstance().enable();
        GamePhaseListener.getInstance().enable();

        GamePhaseCMD.newInstance();
    }

    @Override
    public void disable() {
        GamePhaseListener.getInstance().disable();
        RegularTask.getInstance().disable();
        GamePhaseEngine.getInstance().disable();
    }

    private static class GamePhaseStorage extends JSONPlayerStorage {

        public GamePhaseStorage() {
            super(Main.inst.getPluginID(), StorageEngine.PLAYERS_DATA_FOLDER);
        }

        @Override
        public void saveData() {
            loadFile();

            getConfig().set("General.GamePhase", getPlayer().getGamePhase().getName());

            getConfig().save();
        }

        @Override
        public void loadData() {
            loadFile();

            String gamePhaseName = getConfig().getString("General.GamePhase", null);

            if (gamePhaseName == null) {
                getPlayer().setGamePhase(GamePhaseEngine.DEFAULT_GAME_PHASE);
                LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | У игрока ",
                        getPlayer().getName(), " не найдена игровая фаза: устанавливаем фазу ",
                        getPlayer().getGamePhase().getName());

            } else if (gamePhaseName.equalsIgnoreCase("default")) {
                getPlayer().setGamePhase(GamePhaseEngine.DEFAULT_GAME_PHASE);
                LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | У игрока ",
                        getPlayer().getName(), " найдена игровая фаза: устанавливаем фазу ",
                        getPlayer().getGamePhase().getName());

            } else {
                GamePhase gamePhase = GamePhaseEngine.getGamePhaseByName(gamePhaseName);

                if (gamePhase != null) {
                    getPlayer().setGamePhase(gamePhase);
                    LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | У игрока ",
                            getPlayer().getName(), " найдена игровая фаза: устанавливаем фазу ",
                            gamePhase.getName());
                } else {

                    getPlayer().setGamePhase(GamePhaseEngine.DEFAULT_GAME_PHASE);
                    LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | У игрока ",
                            getPlayer().getName(), " была удалённая игровая фаза: устанавливаем фазу ",
                            getPlayer().getGamePhase().getName());
                }
            }

        }

        @Override
        public PlayerStorageTemplate clone() {
            return new GamePhaseStorage();
        }
    }
}

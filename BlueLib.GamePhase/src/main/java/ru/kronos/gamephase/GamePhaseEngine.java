package ru.kronos.gamephase;

import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.gamephase.onetime.*;
import ru.kronos.gamephase.regular.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GamePhaseEngine extends BlueLibEngine {

    private static GamePhaseEngine inst;
    public final static Map<String, GamePhase> GAME_PHASES = new HashMap<>();
    public static GamePhase DEFAULT_GAME_PHASE = new GamePhase("default");

    private GamePhaseEngine() {}

    public static GamePhaseEngine getInstance() {
        return inst == null ? inst = new GamePhaseEngine() : inst;
    }

    @Override
    public void enable() {
        ConfigurationSection c = Main.config.get();

        Setting.load(c);

        // Инициализация игровых фаз из конфига
        c = c.getConfigurationSection("phases");
        Set<String> phases = c.getKeys(false);
        for (String phaseName : phases) {
            LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | Выполняется загрузка игровой фазы '", phaseName, "'...");

            // Поиск настроек для переопределения конфигурации дефолтной фазы, которая есть всегда.
            if (phaseName.equalsIgnoreCase(DEFAULT_GAME_PHASE.getName())) {
                LogEngine.debugMsg(LoggingLevel.DEBUG,
                        Main.inst.getName(), " | Найдена конфигурация фазы по умолчанию (название = ",
                        DEFAULT_GAME_PHASE.getName(), "). Выполняется переопределение базовых настроек.");

                initGamePhase(DEFAULT_GAME_PHASE, c.getConfigurationSection(DEFAULT_GAME_PHASE.getName()));
                continue;
            }

            GamePhase gamePhase = new GamePhase(phaseName);
            initGamePhase(gamePhase, c.getConfigurationSection(phaseName));
            GAME_PHASES.put(phaseName, gamePhase);
        }

        LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Игровых фаз загружено: ", GAME_PHASES.size(), ".");

        // 10.08.2021 Обновить фазы у игроков, если конфиг был перезагружен во время игры
        OnlineEngine.getOnline().values().forEach(player ->
        {
            GamePhase oldGamePhase = player.getGamePhase();
            player.setGamePhase(GAME_PHASES.getOrDefault(player.getGamePhase().getName(), DEFAULT_GAME_PHASE));
            LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(),
                    " | Обновление настроек игровой фазы у игрока ", player.getName(), " (", oldGamePhase.getName(), " -> ", player.getGamePhase().getName(), ")");
        }
        );
    }

    @Override
    public void disable() {
        GAME_PHASES.clear();
        DEFAULT_GAME_PHASE = new GamePhase("default");
    }

    private static void initGamePhase(GamePhase gamePhase, ConfigurationSection c) {

        // Блок действий join и exit
        String[] modules = new String[] {"join", "exit"};
        for (String moduleName : modules) {
            if (!c.contains(moduleName)) {
                LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | ", c.getName(),
                        " -> Пропуск блока ", moduleName, " (отсутствует)");
                continue;
            } else {
                LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | ", c.getName(),
                        " -> Загрузка блока ", moduleName, "...");
            }

            ConfigurationSection mc = c.getConfigurationSection(moduleName);

            ChangeGamemode.createFromConfig(mc).registerIfInited(gamePhase, moduleName);
            ExecCommands.createFromConfig(mc).registerIfInited(gamePhase, moduleName);
            GiveEffects.createFromConfig(mc).registerIfInited(gamePhase, moduleName);
            SendMessage.createFromConfig(mc).registerIfInited(gamePhase, moduleName);
            SendTitle.createFromConfig(mc).registerIfInited(gamePhase, moduleName);
        }

        // Блок действий regular
        if (c.contains("regular")) {
            LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | ", c.getName(), " -> Загрузка блока regular...");

            ConfigurationSection regular = c.getConfigurationSection("regular");

            BlockRule.createFromConfig(regular).register(gamePhase);
            CommandRule.createFromConfig(regular).register(gamePhase);
            EffectRule.createFromConfig(regular).register(gamePhase);
            GodRule.createFromConfig(regular).register(gamePhase);
            ItemRule.createFromConfig(regular).register(gamePhase);
        } else {
            LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | ", c.getName(), " -> Пропуск блока regular (отсутствует)");
        }
    }

    public static GamePhase getGamePhaseByName(String name) {
        return GAME_PHASES.getOrDefault(name, null);
    }

    public static void changePlayerGamePhase(BlueLibPlayer player, GamePhase gamePhase) {
        player.joinGamePhase(gamePhase);
    }

    public static void clearPlayerGamePhase(BlueLibPlayer player) {
        player.setGamePhase(DEFAULT_GAME_PHASE);
    }
}
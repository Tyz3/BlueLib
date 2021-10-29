package ru.kronos.bluelib.api.engine;

import org.bukkit.Bukkit;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.template.BlueLibEngine;

public final class LogEngine extends BlueLibEngine {

    private static LogEngine inst;

    private LogEngine() {}

    public static LogEngine getInstance() {
        return inst == null ? inst = new LogEngine() : inst;
    }

    @Override
    public void enable() {
        setEnabled(true);
    }

    @Override
    public void disable() {
        setEnabled(false);
    }

    public static void debugMsg(LoggingLevel logLevel, Object... message) {
        if (Main.logLevel.ordinal() < logLevel.ordinal()) return;

        StringBuilder sb = new StringBuilder();
        for (Object s : message) sb.append(s);

        debugMsg(logLevel.getPrefix().concat(" ").concat(sb.toString()));
    }

    public static void debugMsg(LoggingLevel logLevel, String message) {
        if (Main.logLevel.ordinal() < logLevel.ordinal()) return;

        debugMsg(logLevel.getPrefix().concat(" ").concat(message));
    }

    private static void debugMsg(String message) {
        OnlineEngine.CONSOLE_SENDER.sendMessage(message);
    }

    public static void infoLog(String msg) {
        Bukkit.getLogger().info(msg);
    }

    public static void warnLog(String msg) {
        Bukkit.getLogger().warning(msg);
    }
}

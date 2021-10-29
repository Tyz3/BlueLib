package ru.kronos.gamephase;

import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibTask;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.ThreadDaemon;

public class RegularTask extends BlueLibTask {

    private static RegularTask inst;

    private RegularTask() {}

    public static RegularTask getInstance() {
        return inst == null ? inst = new RegularTask() : inst;
    }

    @Override
    public void run() {
        OnlineEngine.getOnline().values().forEach(BlueLibPlayer::tickGamePhase);
    }

    @Override
    public void enable() {
        task = ThreadDaemon.syncTimer(this, 0, Setting.updateRegularAtTicks.getLong());
    }
}

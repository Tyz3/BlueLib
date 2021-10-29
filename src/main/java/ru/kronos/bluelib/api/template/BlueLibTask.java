package ru.kronos.bluelib.api.template;

import org.bukkit.scheduler.BukkitTask;
import ru.kronos.bluelib.api.util.ThreadDaemon;

public abstract class BlueLibTask extends BlueLibEngine implements Runnable {

    protected BukkitTask task;

    public void disable() {
        ThreadDaemon.cancelTask(task);
    }
}

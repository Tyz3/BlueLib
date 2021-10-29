package ru.kronos.bluelib.module.storage;

import ru.kronos.bluelib.Setting;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.StorageEngine;
import ru.kronos.bluelib.api.template.BlueLibTask;
import ru.kronos.bluelib.api.util.ThreadDaemon;
import ru.kronos.bluelib.extra.LoggingLevel;

import java.util.Map;
import java.util.UUID;

public class SaveToDiskTask extends BlueLibTask {

    private static SaveToDiskTask inst;

    private SaveToDiskTask() {}

    public static SaveToDiskTask getInstance() {
        return inst == null ? inst = new SaveToDiskTask() : inst;
    }

    @Override
    public void run() {
        saveToDisk();
    }

    public static void saveToDisk() {
        long t = System.currentTimeMillis();
        long cacheLifeTime = Setting.StorageEngine_saveEverySeconds.getLong() * 1000L;

        int removed = 0;

        for (Map.Entry<UUID, PlayerStorageCache> e : StorageEngine.getInstance().getCachedStorages().entrySet()) {
            PlayerStorageCache cache = e.getValue();

            cache.saveData();
            LogEngine.debugMsg(LoggingLevel.DEBUG, StorageEngine.class.getSimpleName(),
                    " | Данные из кэша сохранены на диск");

            if (t - cache.getTimeStamp() >= cacheLifeTime) {
                StorageEngine.getInstance().removeCache(e.getKey());
                LogEngine.debugMsg(LoggingLevel.DEBUG, StorageEngine.class.getSimpleName(),
                        " | Удаление кэша: время жизни истекло");
                removed++;
            }
        }

        LogEngine.debugMsg(LoggingLevel.INFO, StorageEngine.class.getSimpleName(), " | Данные игроков (",
                StorageEngine.getInstance().getCachedStorages().size(), " записей) сохранены на диск");
        LogEngine.debugMsg(LoggingLevel.INFO, StorageEngine.class.getSimpleName(), " | Данные игроков (",
                removed, " записей) удалены из кэша по истечению времени");
    }

    @Override
    public void enable() {
        task = ThreadDaemon.asyncTimer(
                this,
                20L * Setting.StorageEngine_saveEverySeconds.getLong(),
                20L * Setting.StorageEngine_saveEverySeconds.getLong()
        );
    }
}

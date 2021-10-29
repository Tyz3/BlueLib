package ru.kronos.bluelib.module.storage;

import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.engine.StorageEngine;
import ru.kronos.bluelib.api.template.storage.JSONPlayerStorage;
import ru.kronos.bluelib.api.template.storage.PlayerStorageTemplate;

public final class BlueLibStorage extends JSONPlayerStorage {

    /**
     * Это конкретная реализация - шаблон хранилища, по которому будет происходить
     * чтение и запись данных игрока с файла.
     */
    public BlueLibStorage() {
        // Директория данных игрока для BlueLib
        super(Main.PLUGIN_ID, StorageEngine.PLAYERS_DATA_FOLDER);
    }

    @Override
    public void loadData() {
        loadFile();

        getPlayer().setFirstJoinDate(getConfig().getLong("General.FirstJoinDate", System.currentTimeMillis()));

//			player.setBalance(config.getDouble("Economy.DisplayNameColor", 0D));
    }

    @Override
    public void saveData() {
        loadFile();

        getConfig().set("General.Name", getPlayer().getName());
        getConfig().set("General.UUID", getPlayer().getUUID().toString());
        getConfig().set("General.PlayTime", getPlayer().getPlayTime());
        getConfig().set("General.FirstJoinDate", getPlayer().getFirstJoinDate());

//			config.set("Economy.Balance", player.getBalance());

        getConfig().save();
    }

    @Override
    public PlayerStorageTemplate clone() {
        return new BlueLibStorage();
    }

}

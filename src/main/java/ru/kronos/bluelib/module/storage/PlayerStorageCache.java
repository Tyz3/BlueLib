package ru.kronos.bluelib.module.storage;

import ru.kronos.bluelib.api.template.storage.PlayerStorageTemplate;

import java.util.HashSet;
import java.util.Set;

public final class PlayerStorageCache {
	
	private final Set<PlayerStorageTemplate> STORAGE_SET = new HashSet<>();
	private long timeStamp = Long.MAX_VALUE;
	
	public boolean add(PlayerStorageTemplate playerStorage) {
		return STORAGE_SET.add(playerStorage);
	}
	
	public void clear() {
		STORAGE_SET.clear();
	}
	
	public void loadData() {
		STORAGE_SET.forEach(PlayerStorageTemplate::loadData);
	}

	public void saveData() {
		STORAGE_SET.forEach(PlayerStorageTemplate::saveData);
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	/**
	 * Создание точки отсчёта, когда игрок выходит с сервера.
	 * Через StorageEngine_saveEvery минут после установки точки отсчёта
	 * плагин удалит данные игрока из кэша.
	 */
	public void createTimeStamp() {
		timeStamp = System.currentTimeMillis();
	}
	
	/**
	 * Удаляет точку отсчёта.
	 */
	public void clearTimeStamp() {
		timeStamp = Long.MAX_VALUE;
	}

	@Override
	public String toString() {
		return "PlayerStorageCache{set=".concat(STORAGE_SET.toString())
				.concat(", timeStamp(delta.sec)=")
				.concat(String.valueOf((System.currentTimeMillis() - timeStamp) / 1000L)).concat("}");
	}
}

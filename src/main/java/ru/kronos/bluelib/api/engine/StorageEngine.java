package ru.kronos.bluelib.api.engine;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.storage.PlayerStorageTemplate;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.customevent.ModPlayerJoinEvent;
import ru.kronos.bluelib.module.storage.BlueLibStorage;
import ru.kronos.bluelib.module.storage.PlayerStorageCache;
import ru.kronos.bluelib.api.util.ThreadDaemon;
import ru.kronos.bluelib.module.storage.SaveToDiskTask;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class StorageEngine extends BlueLibEngine implements Listener {
	
	private static StorageEngine inst;

	public static final String PLAYERS_DATA_FOLDER = Main.inst.getDataFolder().getAbsolutePath().concat("/playerdata");

	// Шаблоны сохраняемых данных, которые создаются плагинами и регистрируются в StorageEngine
	private static final Set<PlayerStorageTemplate> PLAYER_STORAGE_TEMPLATES = new HashSet<>();
	private static final Map<UUID, PlayerStorageCache> CACHED_STORAGES = new ConcurrentHashMap<>();
	
	private StorageEngine() {
		// Регистрация макета хранилища (файл для данных игрока) ядра, то есть для самого BlueLib.
		registerNewPlayerStorageTemplate(new BlueLibStorage());
	}
	
	public static StorageEngine getInstance() {
		return inst == null ? inst = new StorageEngine() : inst;
	}
	
	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);
		CACHED_STORAGES.values().forEach(PlayerStorageCache::saveData);

		SaveToDiskTask.getInstance().enable();

		setEnabled(true);
	}
	
	@Override
	public void disable() {
		PlayerJoinEvent.getHandlerList().unregister(this);
		PlayerQuitEvent.getHandlerList().unregister(this);

		SaveToDiskTask.getInstance().disable();
		CACHED_STORAGES.values().forEach(PlayerStorageCache::saveData);

		setEnabled(false);
	}

	/**
	 * Регистрация пользовательских макетов хранилища
	 * @param psTemplate пользовательский макет с реализацией от:
	 *                   JSONPlayerStorage extends PlayerStorageTemplate
	 *                   PlayerStorageTemplate
	 */
	public static void registerNewPlayerStorageTemplate(PlayerStorageTemplate psTemplate) {
		if (!PLAYER_STORAGE_TEMPLATES.add(psTemplate)) {
			LogEngine.debugMsg(LoggingLevel.WARNING, StorageEngine.class.getSimpleName(), " | Повторная регистрация пользовательского хранилища ", psTemplate.getClass().getSimpleName());
		} else {
			LogEngine.debugMsg(LoggingLevel.DEBUG, StorageEngine.class.getSimpleName(), " | Пользовательское хранилище ", psTemplate.getClass().getSimpleName(), " успешно зарегистрировано");
		}
	}

	public Map<UUID, PlayerStorageCache> getCachedStorages() {
		return CACHED_STORAGES;
	}

	public void removeCache(UUID uuid) {
		CACHED_STORAGES.remove(uuid); // .clear()
	}

	public static void forceCacheSave() {
		SaveToDiskTask.saveToDisk();
	}

	private void loadStorageToCacheAndLoadData(PlayerStorageTemplate psTemplate) {
		PlayerStorageCache cache = CACHED_STORAGES.getOrDefault(psTemplate.getPlayer().getUUID(), new PlayerStorageCache());

		if (cache.add(psTemplate)) {
			psTemplate.loadData();
		} else {
			LogEngine.debugMsg(LoggingLevel.WARNING, StorageEngine.class.getSimpleName(), " | Повторная инициализация пользовательского хранилища ", psTemplate.getClass().getSimpleName());
		}

		CACHED_STORAGES.put(psTemplate.getPlayer().getUUID(), cache);
	}

	/**
	 * Главная точка входа, именно здесь регистрируется игрок при входе на сервер.
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerJoinLoadingStorage(PlayerJoinEvent e) {
		BlueLibPlayer player = new BlueLibPlayer(e.getPlayer());
		
		OnlineEngine.addPlayer(player);
		
		ThreadDaemon.async(() -> {
			if (CACHED_STORAGES.containsKey(player.getUUID())) {
				PlayerStorageCache cache = CACHED_STORAGES.get(player.getUUID());
				cache.clearTimeStamp();
				cache.loadData();

				LogEngine.debugMsg(LoggingLevel.INFO, StorageEngine.class.getSimpleName(),
						" | Выполнена загрузка данных из кэша для игрока ", player);
			} else {
				// Инициализация всех шаблонов даных для игрока
				for (PlayerStorageTemplate template : PLAYER_STORAGE_TEMPLATES) {
					PlayerStorageTemplate storage = template.clone();
					// TODO
					storage.loadPlayer(player);
					loadStorageToCacheAndLoadData(storage);

					LogEngine.debugMsg(LoggingLevel.DEBUG, StorageEngine.class.getSimpleName(),
							" | Выполнена загрузка шаблона ", storage.getClass().getSimpleName(),
							" для игрока ", player);
				}

				LogEngine.debugMsg(LoggingLevel.INFO, StorageEngine.class.getSimpleName(),
						" | Выполнена загрузка данных для игрока ", player);
			}
			
			player.setLoaded(true);
			
			ThreadDaemon.sync(() -> Bukkit.getPluginManager().callEvent(new ModPlayerJoinEvent(e, player)));
		});
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	private void onPlayerQuitCachingStorage(PlayerQuitEvent e) {
		PlayerStorageCache cache = CACHED_STORAGES.get(e.getPlayer().getUniqueId());
		cache.createTimeStamp();
		
		OnlineEngine.removePlayer(e.getPlayer());
	}
	
}

package ru.kronos.bluelib.api.template.storage;

import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.extra.PluginID;
import ru.kronos.bluelib.module.storage.PlayerStorageCache;

import java.util.Objects;
import java.util.UUID;

public abstract class PlayerStorageTemplate implements Cloneable {

	private final PluginID pluginID;
	private BlueLibPlayer player;

	public PlayerStorageTemplate(PluginID pluginID) {
		this.pluginID = pluginID;
	}

	public void loadPlayer(BlueLibPlayer player) {
		if (inited()) {
			LogEngine.debugMsg(LoggingLevel.WARNING, PlayerStorageTemplate.class.getCanonicalName(), " | Попытка повторной инициализации хранилища ", this, ".");
		} else {
			this.player = player;
		}
	}

	public BlueLibPlayer getPlayer() {
		assert player != null;
		return player;
	}

	public boolean inited() {
		return player != null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName().concat("{player=").concat(player.getName()).concat("}");
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PlayerStorageTemplate that = (PlayerStorageTemplate) o;
		return pluginID.equals(that.pluginID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(pluginID);
	}

	public abstract PlayerStorageTemplate clone();
	public abstract void saveData();
	public abstract void loadData();
}

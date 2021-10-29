package ru.kronos.bluelib.api.template.storage;

import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.template.config.JSONConfig;
import ru.kronos.bluelib.extra.PluginID;

import java.util.Objects;
import java.util.UUID;

public abstract class JSONPlayerStorage extends PlayerStorageTemplate {
	
	private JSONConfig config;
	private final String path;

	/**
	 * Данные для этого шаблона будут храниться в JSON-файле.
	 * @param path - каталог расположения JSON-файла.
	 */
	public JSONPlayerStorage(PluginID pluginID, String path) {
		super(pluginID);
		this.path = path;
	}

	public void loadFile() {
		config = new JSONConfig(getPlayer().getUUID().toString().concat(".json"), path);
	}

	@Override
	public boolean inited() {
		return super.inited() && config != null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JSONPlayerStorage that = (JSONPlayerStorage) o;
		return config.equals(that.config) && path.equals(that.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(config, path);
	}

	public JSONConfig getConfig() {
		return config;
	}
}

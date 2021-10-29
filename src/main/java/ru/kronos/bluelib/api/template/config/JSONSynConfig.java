package ru.kronos.bluelib.api.template.config;

import java.util.concurrent.ConcurrentHashMap;

public class JSONSynConfig extends JSONConfig {

	public JSONSynConfig(String fileName, String path, boolean compressJson, int capacityListElements) {
		super(fileName, path, compressJson, capacityListElements);
	}

	public JSONSynConfig(String fileName, String path, boolean compressJson) {
		super(fileName, path, compressJson);
	}

	public JSONSynConfig(String fileName, String path) {
		super(fileName, path);
	}

	@Override
	protected void initializeMap() {
		object = new ConcurrentHashMap<>();
	}
}

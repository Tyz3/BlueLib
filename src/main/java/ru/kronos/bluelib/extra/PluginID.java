package ru.kronos.bluelib.extra;

import java.util.Objects;
import java.util.UUID;

public class PluginID {
	
	private final UUID uuid;
	private final int hashCode;
	
	private PluginID(UUID uuid) {
		this.uuid = uuid;
		this.hashCode = hashCode();
	}
	
	public static PluginID randomID() {
		return new PluginID(UUID.randomUUID());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PluginID pluginID = (PluginID) o;
		return hashCode == pluginID.hashCode && uuid.equals(pluginID.uuid);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, hashCode);
	}
	
	@Override
	public String toString() {
		return uuid.toString();
	}
}

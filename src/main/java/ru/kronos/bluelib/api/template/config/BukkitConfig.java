package ru.kronos.bluelib.api.template.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class BukkitConfig {

	private FileConfiguration config = new YamlConfiguration();
	private File configFile;
	private final JavaPlugin instance;
	private final File folder;
	private final String name;
	
	public BukkitConfig(JavaPlugin instance, File folder, String name) {
		this.instance = instance;
		this.folder = folder;
		this.name = name;
	}
	
	public static BukkitConfig create(JavaPlugin instance, File folder, String name) {
		return new BukkitConfig(instance, folder, name);
	}
	
	public static BukkitConfig create(JavaPlugin instance, String name) {
		return new BukkitConfig(instance, instance.getDataFolder(), name);
	}
	
	public void reload() {
		configFile = new File(folder, name);

		if (!configFile.exists() || configFile.length() == 0) {
			if (!configFile.getParentFile().mkdirs()) {
				System.err.println("Не удалось создать директорию " + configFile.getPath() + " для YAML-файла " + configFile.getName() + ".");
			}
			instance.saveResource(name, false);
        }

		config = new YamlConfiguration();

		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		try {
			config.save(configFile);
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public FileConfiguration get() {
        return config;
    }
	
	public ConfigurationSection getConfigurationSection(String arg0) {
		return config.getConfigurationSection(arg0);
	}
}

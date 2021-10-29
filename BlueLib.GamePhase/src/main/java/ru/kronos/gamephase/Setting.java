package ru.kronos.gamephase;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public enum Setting {

	updateRegularAtTicks(100L);
	
	private final boolean[] map = new boolean[] {false, false, false};
	
	private Double number;     // 0
	private boolean bool;      // 1
	private String[] text;     // 2
	
	Setting(Object obj) {
		initObj(this, obj);
	}
	
	private static void initObj(Setting sett, Object obj) {
		
		if (obj == null) {
			System.err.println(Setting.class.getCanonicalName() + " | Ошибка чтения параметра (null). Путь: '" + sett.name().replace("_", ".") + "'.");
			return;
		}
		
		if (obj instanceof List<?>) {
			List<String> list = (ArrayList<String>) obj;
			sett.text = new String[list.size()];
			for (int i = 0; i < list.size(); i++) {
				sett.text[i] = list.get(i).replace("&", "§");
			}
			sett.map[0] = true;
		} else if (obj instanceof String) {
			sett.text = new String[] {obj.toString().replace("&", "§")};
			sett.map[0] = true;
		} else if (obj instanceof Integer || obj instanceof Double || obj instanceof Long) {
			sett.number = Double.parseDouble(obj.toString());
			sett.map[1] = true;
		} else if (obj instanceof Boolean) {
			sett.bool = (Boolean) obj;
			sett.map[2] = true;
		} else {
			System.err.println(Setting.class.getCanonicalName() + " | Ошибка определения типа параметра (" + obj + "). Путь: '" + sett.name().replace("_", ".") + "'.");
		}
	}
	
	public static void load(ConfigurationSection c) {
		for (Setting sett : values()) {
			try {
				
				Object obj = c.get(sett.name().replace("_", "."));
				
				if (obj == null) continue;
				
				initObj(sett, obj);
				
			} catch (NullPointerException e) {
				System.err.println(Setting.class.getCanonicalName() + " | NullPointerException. Path: '"+sett.name().replace("_", ".")+"'.");
				e.printStackTrace();
			} catch (ClassCastException e) {
				System.err.println(Setting.class.getCanonicalName() + " | ClassCastException. Path: '"+sett.name().replace("_", ".")+"'.");
				e.printStackTrace();
			}
		}
	}
	
	public String getString() {
		if (!map[0]) {
			System.err.println(Setting.class.getCanonicalName() + " | String " + Setting.this + " isn't defined.");
			return "empty";
		} else return Setting.this.text[0];
	}
	
	public List<String> getList() {
		if (!map[0]) {
			System.err.println(Setting.class.getCanonicalName() + " | String[] " + Setting.this + " isn't defined.");
			return Collections.singletonList("empty");
		} else return Arrays.asList(Setting.this.text);
	}
	
	public String[] getArray() {
		if (!map[0]) {
			System.err.println(Setting.class.getCanonicalName() + " | String[] " + Setting.this + " isn't defined.");
			return new String[] {"empty"};
		} else return Setting.this.text;
	}

	public long getLong() {
		if (!map[1]) {
			System.err.println(Setting.class.getCanonicalName() + " | Long " + Setting.this + " isn't defined.");
			return -1L;
		} else return Setting.this.number.longValue();
	}

	public int getInt() {
		if (!map[1]) {
			System.err.println(Setting.class.getCanonicalName() + " | Integer " + Setting.this + " isn't defined.");
			return -1;
		} else return Setting.this.number.intValue();
	}

	public double getDouble() {
		if (!map[1]) {
			System.err.println(Setting.class.getCanonicalName() + " | Double " + Setting.this + " isn't defined.");
			return -1D;
		} else return Setting.this.number;
	}

	public boolean getBool() {
		if (!map[2]) {
			System.err.println(Setting.class.getCanonicalName() + " | Boolean " + Setting.this + " isn't defined.");
			return false;
		} else return Setting.this.bool;
	}
}

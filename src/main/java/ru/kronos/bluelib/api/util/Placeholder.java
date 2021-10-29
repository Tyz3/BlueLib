package ru.kronos.bluelib.api.util;

import java.util.List;

public class Placeholder {
	
	public static String set(String source, String... args) {
		if (args != null && args.length % 2 == 0 && args.length > 1) {
			for (int i = 0; i < args.length; i+=2) {
				if (args[i] == null) break;
				if (args[i+1] == null) continue;
				source = source.replace(args[i], args[i+1]);
			}
		}
		return source;
	}
	
	public static List<String> set(List<String> source, String... args) {
		for (int i = 0; i < source.size(); i++) {
			 source.set(i, set(source.get(i), args));
		}
		return source;
	}
}

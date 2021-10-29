package ru.kronos.bluelib.extra;

public enum LoggingLevel {
	
	MINIMUM, CRITICAL, ERROR, WARNING, INFO, DEBUG;
//	0        1         2      3        4     5
	
	private String prefix = "";

	public String getPrefix() {
		return prefix;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public static LoggingLevel matchOrDefault(String s, LoggingLevel logLevel) {
		if (s == null) return logLevel;
		for (LoggingLevel ll : values()) {
			if (ll.name().equalsIgnoreCase(s))
				return ll;
		}
		return logLevel;
	}
}

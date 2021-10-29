package ru.kronos.bluelib.api.util;

public class Time {
	
	private final long timeMillis;
	private long days;
	private long hours;
	private long minutes;
	private long seconds;
	
	private Time(long timeMillis) {
		this.timeMillis = timeMillis;
		work();
	}
	
	public static Time create(long timeMillis) {
		return new Time(timeMillis);
	}
	
	private void work() {
		long temp = timeMillis / 1000;
		days = 0;
		hours = temp/3600;
		if (hours >= 24) {
			days = hours / 24; // получаем дни
			hours %= 24; // вычитаем дни из часов
		}
		long rem = temp % 3600; // получаем остаток минут и секунд
		minutes = rem / 60;
		seconds = rem % 60;
	}
	
	/**
	 * Заменяет следующие плейсхолдеры в str:
	 * {D} - дни, {H} - часы, {M} - минуты, {S} - секунды.
	 */
	public String toFormat(String str) {
		return str.replace("{D}", String.valueOf(days)).replace("{H}", String.valueOf(hours))
				.replace("{M}", String.valueOf(minutes)).replace("{S}", String.valueOf(seconds));
	}
}

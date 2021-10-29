package ru.kronos.bluelib.module.request;

import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class RequestSystem implements Runnable {
	
	public static Map<String, RequestTask> requests = new ConcurrentHashMap<>();
	
	
	
	@Override
	public void run() {
		// Задание очистки.
		
		int var1 = 0;
		for (Entry<String, RequestTask> e : requests.entrySet()) {
			
			if (e.getValue().ended()) {
				requests.remove(e.getKey());
				var1--;
			}
			
		}

		LogEngine.debugMsg(LoggingLevel.DEBUG, RequestSystem.class.getSimpleName(), " | Выполнена очистка истёкщих и выполненых запросов (", String.valueOf(var1), ").");
	}
	
	public static RequestTask getRequestTask(String playerName, String requestName) {
		if (requests.isEmpty()) return null;
		String key = playerName.concat("-").concat(requestName);
		if (requests.containsKey(key)) {
			RequestTask rt = requests.get(key);
			return rt.ended() ? null : rt;
		}
		return null;
	}
}

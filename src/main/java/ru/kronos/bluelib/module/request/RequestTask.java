package ru.kronos.bluelib.module.request;

import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;

public class RequestTask {
	
	private final Runnable r;
	private final long expired;
	private boolean worked = false;
	
	public RequestTask(Runnable r, long lifetime) {
		this.r = r;
		this.expired = System.currentTimeMillis() + lifetime*1000;
	}
	
	public void run(String playerName, String actionName) {
		r.run();
		worked = true;

		LogEngine.debugMsg(LoggingLevel.DEBUG, RequestTask.class.getSimpleName(), " | Активирован запрос (", playerName, ", ", actionName, ").");
	}
	
	private boolean worked() {
		return worked;
	}
	
	private boolean expired() {
		return expired < System.currentTimeMillis();
	}
	
	public boolean ended() {
		return worked() || expired();
	}
}

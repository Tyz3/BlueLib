package ru.kronos.bluelib.api.engine;

import ru.kronos.bluelib.api.template.BlueLibEngine;

public final class ScoreboardEngine extends BlueLibEngine {

	private static ScoreboardEngine inst;

	private ScoreboardEngine() {}

	public static ScoreboardEngine getInstance() {
		return inst == null ? inst = new ScoreboardEngine() : inst;
	}
	
	@Override
	public void enable() {
		setEnabled(true);
	}
	
	@Override
	public void disable() {

		setEnabled(false);
	}
}

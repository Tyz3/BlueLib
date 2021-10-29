package ru.kronos.bluelib.api.engine;

import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.command.BlueLibCMD;

public final class CommandEngine extends BlueLibEngine {

	private static CommandEngine inst;

	private CommandEngine() {}

	public static CommandEngine getInstance() {
		return inst == null ? inst = new CommandEngine() : inst;
	}
	
	@Override
	public void enable() {
		BlueLibCMD.newInstance();
	}

	@Override
	public void disable() {}

}

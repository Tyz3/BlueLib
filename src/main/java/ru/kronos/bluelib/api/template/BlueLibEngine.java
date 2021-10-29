package ru.kronos.bluelib.api.template;

public abstract class BlueLibEngine {

	private static int registeredAmount = 0;

	private boolean enabled = false;

	protected BlueLibEngine() {
		registeredAmount++;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public static int getRegisteredAmount() {
		return registeredAmount;
	}

	/**
	 * Выполняется только при запуске (1 раз).
	 */
	public abstract void enable();

	/**
	 * Выполняется только при выключении (1 раз).
	 */
	public abstract void disable();

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}

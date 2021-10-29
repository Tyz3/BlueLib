package ru.kronos.bluelib.module.profiler;

class Sample {
	
	private long time;
	private StackTraceElement[] stack;
	
	public Sample(StackTraceElement[] stack, long time) {
		this.time = time;
		this.stack = stack;
	}
	
	public StackTraceElement[] getStackTrace() {
		return stack;
	}
	
	public long getTime() {
		return time;
	}
	
}

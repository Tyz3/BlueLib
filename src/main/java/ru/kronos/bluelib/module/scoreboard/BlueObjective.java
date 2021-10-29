package ru.kronos.bluelib.module.scoreboard;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BlueObjective {
	
	private final String objectiveName;
	private final Objective objective;
	private final Trigger trigger;
	private final DisplaySlot displaySlot;
	
	public static BlueObjective createNewObjective(BlueScoreboard scoreboard, String objectiveName, String title, DisplaySlot displaySlot, Trigger trigger) {
		return new BlueObjective(scoreboard, objectiveName, title, displaySlot, trigger);
	}
	
	private BlueObjective(BlueScoreboard scoreboard, String objectiveName, String displayName, DisplaySlot displaySlot, Trigger trigger) {
		objective = scoreboard.getBukkitScoreboard().registerNewObjective(objectiveName, trigger.name());
		objective.setDisplaySlot(displaySlot);
		objective.setDisplayName(displayName);
		
		this.objectiveName = objectiveName;
		this.trigger = trigger;
		this.displaySlot = displaySlot;
	}
	
	public BlueObjective(Objective objective) {
		this.objectiveName = objective.getName();
		this.objective = objective;
		this.trigger = Trigger.valueOf(objective.getCriteria());
		this.displaySlot = objective.getDisplaySlot();
	}
	
	public void unregister() {
		objective.unregister();
	}
	
	public String getName() {
		return objectiveName;
	}
	
	public Trigger getTrigger() {
		return trigger;
	}
	
	public DisplaySlot getDisplaySlot() {
		return displaySlot;
	}
	
	public Objective getBukkitObjective() {
		return objective;
	}
	
	public String getDisplayName() {
		return objective.getDisplayName();
	}
	
	public Score getScore(String byValue) {
		return objective.getScore(byValue);
	}
	
	public BlueObjective resetScores(String byValue) {
		objective.getScoreboard().resetScores(byValue);
		return this;
	}
	
	public BlueObjective setScore(String byValue, int value) {
		if (byValue.length() > 48) byValue = byValue.substring(0, 48);
		getScore(byValue).setScore(value);
		return this;
	}
	
	public BlueObjective incScore(String byValue, int value) {
		Score s = getScore(byValue);
		s.setScore(s.getScore() + value);
		return this;
	}
	
	public BlueObjective incScore(String byValue) {
		return incScore(byValue, 1);
	}
	
	public BlueObjective decScore(String byValue) {
		return incScore(byValue, -1);
	}
	
	private final Map<String, Integer> sidebarLines = new LinkedHashMap<>();
	
	public BlueObjective blankLine() {
		add("");
		return this;
	}
	
	public BlueObjective add(String text) {
		text = fixDuplicates(text);
		put(text);
		return this;
	}
	
	public BlueObjective put(String text) {
		put(text, null);
		return this;
	}
	
	public BlueObjective put(String text, Integer score) {
		if (text.length() > 48) text = text.substring(0, 48);
		sidebarLines.put(text, score);
		return this;
	}
	
	private String fixDuplicates(String text) {
		while (sidebarLines.containsKey(text)) text = text.concat("§r");
		return text;
	}
	
	public void build() {
		if (objective.getDisplaySlot() == DisplaySlot.SIDEBAR) {
			int size = sidebarLines.size();
			for (Entry<String, Integer> e : sidebarLines.entrySet()) {
				Integer value = e.getValue();
				objective.getScore(e.getKey()).setScore(value == null ? size : value);
				size--;
			}
		} else {
			LogEngine.debugMsg(LoggingLevel.WARNING, BlueObjective.class.getSimpleName(), " | Objective должен иметь DisplaySlot.SIDEBAR, чтобы использовать build().");
		}
	}
	
}

package ru.kronos.bluelib.module.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

import java.util.List;
import java.util.Set;

public class BlueScoreboard {

	private Scoreboard sb;
	
	public BlueScoreboard() {
		sb = Bukkit.getScoreboardManager().getNewScoreboard();
	}
	
	public BlueScoreboard(Scoreboard scoreboard) {
		sb = scoreboard;
	}
	
	public BlueTeam registerNewTeam(String teamName) {
		return registerNewTeam(teamName, true, false);
	}
	
	public BlueTeam registerNewTeam(String teamName, boolean seeFriendlyInvisibles, boolean allowFriendlyFire) {
		return BlueTeam.createNewTeam(this, teamName, seeFriendlyInvisibles, allowFriendlyFire);
	}
	
	public BlueObjective registerNewObjective(String objectiveName, String title, DisplaySlot displaySlot, Trigger trigger) {
		return BlueObjective.createNewObjective(this, objectiveName, title, displaySlot, trigger);
	}
	
	public Scoreboard getBukkitScoreboard() {
		return sb;
	}
	
	public Set<Team> getTeams() {
		return sb.getTeams();
	}
	
	public Set<Objective> getObjectives() {
		return sb.getObjectives();
	}
	
	public BlueObjective getObjective(DisplaySlot displaySlot) {
		Objective o = sb.getObjective(displaySlot);
		if (o == null) {
			LogEngine.debugMsg(LoggingLevel.CRITICAL, BlueScoreboard.class.getName(), " | Ошибка Objective ", displaySlot, " не существует.");
			return null;
		} else return new BlueObjective(o);
	}
	
	public BlueTeam getTeam(String teamName) {
		Team t = sb.getTeam(teamName);
		if (t == null) {
			LogEngine.debugMsg(LoggingLevel.CRITICAL, BlueScoreboard.class.getName(), " | Ошибка Team ", teamName, " не существует.");
			return null;
		} else return new BlueTeam(t);
	}
	
	public boolean hasObjective(DisplaySlot displaySlot) {
		return sb.getObjective(displaySlot) != null;
	}
	
	public boolean hasTeam(String teamName) {
		return sb.getTeam(teamName) != null;
	}
	
	public void unregAll() {
		unregTeams();
		unregObjectives();
	}
	
	public void unregTeams() {
		getTeams().forEach(Team::unregister);
	}
	
	public void unregObjectives() {
		getObjectives().forEach(Objective::unregister);
	}
	
	public void unregTeam(String teamName) {
		Team t = sb.getTeam(teamName);
		if (t != null) t.unregister();
	}
	
	public void unregObjective(DisplaySlot displaySlot) {
		Objective o = sb.getObjective(displaySlot);
		if (o != null) o.unregister();
	}
	
	public void send(BlueLibPlayer... players) {
		for (BlueLibPlayer p : players) p.setScoreboard(this);
	}
	
	public void send(List<BlueLibPlayer> players) {
		players.forEach(p -> p.setScoreboard(this));
	}
	
	@Deprecated
	public void send(Player... players) {
		for (Player p : players) OnlineEngine.getPlayer(p).setScoreboard(this);
	}
	
}

package ru.kronos.bluelib.module.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlueTeam {
	
	private String teamName;
	private Team team;
	
	public static BlueTeam createNewTeam(BlueScoreboard sb, String teamName, boolean seeFriendlyInvisibles, boolean allowFriendlyFire) {
		return new BlueTeam(sb.getBukkitScoreboard(), teamName, seeFriendlyInvisibles, allowFriendlyFire);
	}
	
	private BlueTeam(Scoreboard sb, String teamName, boolean seeFriendlyInvisibles, boolean allowFriendlyFire) {
		team = sb.registerNewTeam(teamName);
		team.setCanSeeFriendlyInvisibles(seeFriendlyInvisibles);
		team.setAllowFriendlyFire(allowFriendlyFire);
		
		this.teamName = teamName;
	}
	
	public BlueTeam(Team team) {
		this.teamName = team.getName();
		this.team = team;
	}
	
	public void unregister() {
		team.unregister();
	}
	
	public String getTeamName() {
		return teamName;
	}
	
	public Team getBukkitTeam() {
		return team;
	}
	
	public BlueTeam setPrefix(String value) {
		team.setPrefix(value);
		return this;
	}
	
	public BlueTeam setSuffix(String value) {
		team.setSuffix(value);
		return this;
	}
	
	public BlueTeam setDisplayname(String value) {
		team.setDisplayName(value);
		return this;
	}
	
	public BlueTeam addEntry(String entry) {
		team.addPlayer(Bukkit.getOfflinePlayer(entry));
		return this;
	}
	
	public BlueTeam removeEntry(String entry) {
		team.removePlayer(Bukkit.getOfflinePlayer(entry));
		return this;
	}
	
	public BlueTeam addEntries(String... entries) {
		for (int i = 0; i < entries.length; i++) {
			addEntry(entries[i]);
		}
		return this;
	}
	
	public BlueTeam addEntries(List<String> entries) {
		entries.forEach(value -> addEntry(value));
		return this;
	}
	
	public BlueTeam removeEntries() {
		team.getPlayers().forEach(entry -> removeEntry(entry.getName()));
		return this;
	}
	
	public Set<String> getEntries() {
		Set<String> set = new HashSet<>();
		team.getPlayers().forEach(entry -> set.add(entry.getName()));
		return set;
	}
}

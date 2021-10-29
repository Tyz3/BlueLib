package ru.kronos.bluelib.module.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.DisplaySlot;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.module.customevent.ModPlayerJoinEvent;

public class ScoreboardTesting implements Listener {
	
	
	public ScoreboardTesting() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);
	}
	
	public static void make() {
		new ScoreboardTesting();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(ModPlayerJoinEvent e) {
		BlueScoreboard sc = new BlueScoreboard();
		
		sc.registerNewObjective("Food", "s", DisplaySlot.PLAYER_LIST, Trigger.food);
		BlueObjective o = sc.registerNewObjective("TEST", "Привет", DisplaySlot.SIDEBAR, Trigger.dummy);
		
		o.add(e.getEventName());
		o.blankLine();
		o.blankLine();
		o.add(e.getPlayer().getDisplayName());
		o.build();
		
		BlueTeam t = sc.registerNewTeam("Kronos Team", true, true);
		
		t.addEntries(e.getPlayer().getName());
		
		sc.send(e.getPlayer());
		
		e.getPlayer().getScoreboard();
		
		
 	}
}

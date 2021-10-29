package ru.kronos.bluelib.module.warmup;

import org.bukkit.Location;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.Message;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.WarmUpEngine;
import ru.kronos.bluelib.api.util.CountingPattern;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.MathOperation;

import java.util.Arrays;
import java.util.List;

public class WarmUpTask implements Runnable {
	
	private final Runnable r;
	private final BlueLibPlayer player;
	private long delaySeconds;
	private final CountingPattern pattern;
	private final boolean move;
	private final Location activationPlace;
	private final List<String> notificationMessage;
	
	private boolean ended = false;
	
	public WarmUpTask(Runnable r, BlueLibPlayer player, long delaySeconds, CountingPattern pattern, boolean move, List<String> notificationMessage) {
		this.r = r;
		this.player = player;
		this.delaySeconds = delaySeconds;
		this.pattern = pattern;
		this.move = move;
		
		this.activationPlace = player.getLocation();
		this.notificationMessage = notificationMessage;
	}
	
	public WarmUpTask(Runnable r, BlueLibPlayer player, long delaySeconds, CountingPattern pattern, boolean move, String[] notificationMessage) {
		this(r, player, delaySeconds, pattern, move, Arrays.asList(notificationMessage));
	}
	
	public WarmUpTask(Runnable r, BlueLibPlayer player, long delaySeconds, CountingPattern pattern, boolean move) {
		this(r, player, delaySeconds, pattern, move, Message.WarmUpEngine_notification.gets());
	}
	
	public WarmUpTask(Runnable r, BlueLibPlayer player, long delaySeconds, String pattern, boolean move) {
		this(r, player, delaySeconds, new CountingPattern(pattern), move);
	}

	@Override
	public String toString() {
		return "[ Runnable = " + r + ", Player = " + player + ", Delay = " + delaySeconds + ", Pattern = " + pattern + ", CanMove = " + move + ", ActivationLoc = " + activationPlace + " ]";
	}
	
	@Override
	public void run() {

		if (delaySeconds <= 0) {
			r.run();
			stop();
			LogEngine.debugMsg(LoggingLevel.DEBUG, WarmUpEngine.class.getSimpleName(), " | Выполнено отложенное действие для ", player.getDisplayName(), ".");
			return;
		}
		
		if (player == null || !player.getBukkitPlayer().isOnline()) {
			stop();
			return;
		}
		
		if (onMove()) {
			stop();
			Message.WarmUpEngine_cancelled_move.send(player);
			return;
		}
		
		if (pattern != null && pattern.contains(delaySeconds)) {
			player.sendMessages(notificationMessage, "{S}", String.valueOf(delaySeconds));
		}
		
		delaySeconds--;
	}
	
	private void stop() {
		ended = true;
	}
	
	public boolean onMove() {
		return !canMove() && (
				MathOperation.distance2D(activationPlace, player.getLocation()) > 0
						|| activationPlace.getWorld() != player.getWorld()
		);
	}
	
	public boolean isEnded() {
		return ended;
	}
	
	public boolean canMove() {
		return move;
	}
}

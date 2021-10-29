package ru.kronos.combatlog;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import ru.kronos.bluelib.api.template.BlueLibListener;
import ru.kronos.bluelib.module.customevent.*;
import ru.kronos.bluelib.api.util.MathOperation;

public class CombatLogListener extends BlueLibListener {

	private static CombatLogListener inst;

	public static CombatLogListener getInstance() {
		return inst == null ? inst = new CombatLogListener() : inst;
	}
	
	private CombatLogListener() {}

	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);
	}

	@Override
	public void disable() {
		PlayerQuitEvent.getHandlerList().unregister(this);
		PlayerShotPlayerEvent.getHandlerList().unregister(this);
		PlayerHitPlayerEvent.getHandlerList().unregister(this);
		PlayerShotMobEvent.getHandlerList().unregister(this);
		PlayerHitMobEvent.getHandlerList().unregister(this);
		MobHitPlayerEvent.getHandlerList().unregister(this);
		MobShotPlayerEvent.getHandlerList().unregister(this);
		PlayerTeleportEvent.getHandlerList().unregister(this);
		PlayerDeathEvent.getHandlerList().unregister(this);
		PlayerKickEvent.getHandlerList().unregister(this);
		PlayerCommandPreprocessEvent.getHandlerList().unregister(this);
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerQuit(PlayerQuitEvent e) {
		
		if (CombatLogEngine.hasCombat(e.getPlayer())) {
			e.getPlayer().setHealth(0D);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerShotPlayer(PlayerShotPlayerEvent e) {
		if (CombatLogEngine.playersExempt) return;

		Player damager = e.getDamager();
		Player victim = e.getVictim();
		
		CombatLogEngine.claimCombatWithPlayer(damager, victim);
		CombatLogEngine.claimCombatWithPlayer(victim, damager);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerHitPlayer(PlayerHitPlayerEvent e) {
		if (CombatLogEngine.playersExempt) return;

		Player damager = e.getDamager();
		Player victim = e.getVictim();
		
		CombatLogEngine.claimCombatWithPlayer(damager, victim);
		CombatLogEngine.claimCombatWithPlayer(victim, damager);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerShotMob(PlayerShotMobEvent e) {
		if (CombatLogEngine.mobsExempt) return;
		
		CombatLogEngine.claimCombatWithMob(e.getDamager());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerHitMob(PlayerHitMobEvent e) {
		if (CombatLogEngine.mobsExempt) return;
		
		CombatLogEngine.claimCombatWithMob(e.getDamager());
	}


	@EventHandler(priority = EventPriority.HIGHEST)
	private void onMobHitPlayer(MobHitPlayerEvent e) {
		if (CombatLogEngine.mobsExempt) return;
		
		CombatLogEngine.claimCombatWithMob(e.getVictim());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onMobShotPlayer(MobShotPlayerEvent e) {
		if (CombatLogEngine.mobsExempt) return;
		
		if (e.getDamager() instanceof LivingEntity)
			CombatLogEngine.claimCombatWithMob(e.getVictim());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void onPlayerTeleport(PlayerTeleportEvent e) {
		if (e.getCause() == TeleportCause.UNKNOWN) return;
		
		if (CombatLogEngine.disableTeleports && CombatLogEngine.hasCombat(e.getPlayer())) {
			e.setCancelled(
					MathOperation.distance2D(e.getFrom(), e.getTo()) > CombatLogEngine.maxTeleportDistance
			);
		}
	}
	
	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent e) {
		CombatLogEngine.removeCombat(e.getEntity());
	}

	@EventHandler
	private void onPlayerKick(PlayerKickEvent e) {
		if (CombatLogEngine.kickExempt) return;
		
		if (CombatLogEngine.hasCombat(e.getPlayer())) {
			e.getPlayer().setHealth(0D);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {
		
		if (!CombatLogEngine.hasCombat(e.getPlayer())) return;
		if (CombatLogEngine.allowedCmds.isEmpty()) return;
		
		e.setCancelled(true);
		
		for (String cmd : CombatLogEngine.allowedCmds) {
			if (e.getMessage().startsWith(cmd)) {
				e.setCancelled(false);
				break;
			}
		}
		
		if (e.isCancelled()) {
			Message.cmdBlocked.send(e.getPlayer());
		}
	}
}

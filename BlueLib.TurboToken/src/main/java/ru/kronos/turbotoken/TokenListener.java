package ru.kronos.turbotoken;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityShootBowEvent;
import ru.kronos.bluelib.module.customevent.*;

public class TokenListener implements Listener {
	
	public TokenListener() {
		Bukkit.getPluginManager().registerEvents(this, Main.inst);
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(ModPlayerJoinEvent e) {
		
		Main.ONLINE.put(e.getPlayer().getUUID(), new TurboPlayer(e.getPlayer()));
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onHealthRegain(EntityRegainHealthEvent e) {
		
		if (e.getRegainReason() == RegainReason.SATIATED) {
			TurboPlayer player = Main.getTurboPlayer((Player) e.getEntity());
			TokenEngine.regainHealth(e, player, e.getAmount());
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDeath(EntityDeathEvent e) {
		
		if (e.getEntity() instanceof Player) {
			TurboPlayer victim = Main.getTurboPlayer((Player) e.getEntity());
			
			if (e.getEntity().getKiller() != null) {
				TurboPlayer killer = Main.getTurboPlayer(e.getEntity().getKiller());
				TokenEngine.playerKillsPlayer(e, killer, victim);
			} else {
				// TODO Проверить работу. Киллер в этом событии всегда Player?
				TokenEngine.mobKillsPlayer(e, e.getEntity().getKiller(), victim);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityShootBow(EntityShootBowEvent e) {
		
		// Поджигаем стрелу, если у игрока есть нужный чар.
		if (e.getEntity() instanceof Player) {
			TurboPlayer shooter = Main.getTurboPlayer((Player) e.getEntity());
			shooter.invokeRangeTraits(e.getProjectile(), null);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamageEvent(EntityDamageEvent e) {
		
		if (e.getEntity() instanceof Player) {
			TurboPlayer victim = Main.getTurboPlayer((Player) e.getEntity());
			
			switch (e.getCause()) {
			case FALL:
				TokenEngine.fallDamageToPlayer(e, victim, e.getDamage());
				break;
			case ENTITY_ATTACK:
				TokenEngine.unknownDamageToPlayer(e, victim, e.getDamage());
				break;
			case BLOCK_EXPLOSION: case ENTITY_EXPLOSION:
				TokenEngine.explosionDamageToPlayer(e, victim, e.getDamage());
				break;
			default: break;
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerHitPlayer(PlayerHitPlayerEvent e) {
		
		TokenEngine.playerHitPlayer(e, Main.getTurboPlayer(e.getDamager()), Main.getTurboPlayer(e.getVictim()), e.getDamage());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerHitMob(PlayerHitMobEvent e) {
		
		TokenEngine.playerHitMob(e, Main.getTurboPlayer(e.getDamager()), e.getVictim(), e.getDamage());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMobHitPlayer(MobHitPlayerEvent e) {
		
		TokenEngine.mobHitPlayer(e, e.getDamager(), Main.getTurboPlayer(e.getVictim()), e.getDamage());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShotPlayer(PlayerShotPlayerEvent e) {
		
		TokenEngine.playerShotPlayer(e, e.getProjectile(), Main.getTurboPlayer(e.getDamager()), Main.getTurboPlayer(e.getVictim()), e.getDamage());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerShotMob(PlayerShotMobEvent e) {
		TokenEngine.playerShotMob(e, e.getProjectile(), Main.getTurboPlayer(e.getDamager()), e.getVictim(), e.getDamage());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMobShotPlayer(MobShotPlayerEvent e) {
		TokenEngine.mobShotPlayer(e, (LivingEntity) e.getDamager(), Main.getTurboPlayer(e.getVictim()), e.getDamage());
	}
	
	
}

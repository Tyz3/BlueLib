package ru.kronos.bluelib.junk;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
	
	public enum EventPriority {
		CORE_F, NICE_0, NICE_1, NICE_2, NICE_3, NICE_4, NICE_5, NICE_6, NICE_7, NICE_8, NICE_9,
		NICE_10, NICE_11, NICE_12, NICE_13, NICE_14, NICE_15, NICE_16, NICE_17, NICE_18, NICE_19, CORE_L;
	}

	public enum EventType {
		OPTIMIZED, PARALLEL;
	}
	
	public enum EventName {
		AsyncPlayerChatEvent, PlayerAdvancementDoneEvent, PlayerAnimationEvent, PlayerBedEnterEvent, 
		PlayerBedLeaveEvent, PlayerBucketEvent, PlayerChangedMainHandEvent, PlayerChangedWorldEvent, 
		PlayerChannelEvent, PlayerChatEvent, PlayerChatTabCompleteEvent, PlayerCommandPreprocessEvent, 
		PlayerCommandSendEvent, PlayerDropItemEvent, PlayerEditBookEvent, PlayerEggThrowEvent, 
		PlayerExpChangeEvent, PlayerFishEvent, PlayerGameModeChangeEvent, PlayerHarvestBlockEvent, 
		PlayerInteractEntityEvent, PlayerInteractEvent, PlayerItemBreakEvent, PlayerItemConsumeEvent, 
		PlayerItemDamageEvent, PlayerItemHeldEvent, PlayerItemMendEvent, PlayerJoinEvent, PlayerKickEvent, 
		PlayerLevelChangeEvent, PlayerLocaleChangeEvent, PlayerLoginEvent, PlayerMoveEvent, PlayerPickupItemEvent, 
		PlayerQuitEvent, PlayerRecipeDiscoverEvent, PlayerResourcePackStatusEvent, PlayerRespawnEvent, 
		PlayerRiptideEvent, PlayerShearEntityEvent, PlayerSpawnLocationEvent, PlayerStatisticIncrementEvent, 
		PlayerSwapHandItemsEvent, PlayerTakeLecternBookEvent, PlayerToggleFlightEvent, PlayerToggleSneakEvent, 
		PlayerToggleSprintEvent, PlayerVelocityEvent, PlayerTeleportEvent, PlayerDeathEvent,
		
		InventoryClickEvent, InventoryCloseEvent, InventoryOpenEvent,
		
		// Custom
		PlayerHitPlayerEvent, PlayerShotPlayerEvent, PlayerHitEntityEvent, PlayerShotEntityEvent,
		EntityHitPlayerEvent, EntityShotPlayerEvent, EntityHitEntityEvent, EntityShotEntityEvent;
	}
	
	public EventPriority priority() default EventPriority.NICE_10;
//	public EventType type() default EventType.PARALLEL;
	public EventName name();
	public boolean ignoreCancelled() default false;
	
}

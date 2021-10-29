package ru.kronos.bluelib.api.engine;

import github.scarsz.discordsrv.DiscordSRV;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.module.api.DiscordEvents;
import ru.kronos.bluelib.module.customevent.ModPlayerJoinEvent;

public final class API3rdPartyEngine extends BlueLibEngine implements Listener {

	private static API3rdPartyEngine inst;

	private API3rdPartyEngine() {}

	public static API3rdPartyEngine getInstance() {
		return inst == null ? inst = new API3rdPartyEngine() : inst;
	}

	private static boolean DiscordSRVEnable;
	private static boolean PlaceholderAPIEnable;
	private static boolean PermissionExEnable;
	private static boolean LuckPermsEnable;
	private static boolean CombatLogEnable;

	@Override
	public void enable() {

		Bukkit.getPluginManager().registerEvents(getInstance(), Main.inst);
		
		if (Bukkit.getPluginManager().isPluginEnabled("DiscordSRV")) {
			DiscordSRVEnable = true;
			DiscordEvents.newInstance();
			LogEngine.debugMsg(LoggingLevel.MINIMUM, "[3rdParty] Включена поддержка DiscordSRV.");
		}
		
		if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
			PlaceholderAPIEnable = true;
			LogEngine.debugMsg(LoggingLevel.MINIMUM, "[3rdParty] Включена поддержка PlaceholderAPI.");
		}
		
		if (Bukkit.getPluginManager().isPluginEnabled("PermissionEx")) {
			PermissionExEnable = true;
			LogEngine.debugMsg(LoggingLevel.MINIMUM, "[3rdParty] Включена поддержка PermissionEx.");
		}
		
		if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
			LuckPermsEnable = true;
			LogEngine.debugMsg(LoggingLevel.MINIMUM, "[3rdParty] Включена поддержка LuckyPerms.");
		}
		
		if (Bukkit.getPluginManager().isPluginEnabled("BlueLib.CombatLog")) {
			CombatLogEnable = true;
			LogEngine.debugMsg(LoggingLevel.MINIMUM, "[3rdParty] Включена поддержка BlueLib.CombatLog.");
		}

	}

	@Override
	public void disable() {
		ModPlayerJoinEvent.getHandlerList().unregister(getInstance());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	private void LuckPermsLoadOnJoin(ModPlayerJoinEvent e) {
		BlueLibPlayer p = e.getPlayer();

		if (enabledLuckPerms()) {
			UserManager m = getLuckyPermsManager().getUserManager();
			User u = m.getUser(p.getUUID());

			assert u != null;
			CachedMetaData meta = u.getCachedData().getMetaData();

			p.setPrefix(meta.getPrefix());
			p.setSuffix(meta.getSuffix());
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	private void DiscordSRVLoadOnJoin(ModPlayerJoinEvent e) {
		BlueLibPlayer p = e.getPlayer();

		if (enabledDiscordSRV()) {
			if (DiscordSRV.isReady) {
				String discordId = DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(p.getUUID());
				p.setDiscordId(discordId);
			}
		}
	}

	public static boolean enabledDiscordSRV() {
		return DiscordSRVEnable;
	}

	public static boolean enabledPlaceholderAPI() {
		return PlaceholderAPIEnable;
	}

	public static boolean enabledPermissionEx() {
		return PermissionExEnable;
	}

	public static boolean enabledLuckPerms() {
		return LuckPermsEnable;
	}
	
	public static LuckPerms getLuckyPermsManager() {
		return LuckPermsProvider.get();
	}
	
	public static boolean enabledCombatLog() {
		return CombatLogEnable;
	}
	

}

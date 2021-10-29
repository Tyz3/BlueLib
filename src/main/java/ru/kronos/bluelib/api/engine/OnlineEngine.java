package ru.kronos.bluelib.api.engine;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.Message;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.template.online.BlueLibSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class OnlineEngine extends BlueLibEngine {

	private static OnlineEngine inst;

	private static final Map<UUID, BlueLibPlayer> online = new ConcurrentHashMap<>();
	
	public static final BlueLibSender CONSOLE_SENDER = new BlueLibSender(Bukkit.getServer().getConsoleSender());

	private OnlineEngine() {}

	public static OnlineEngine getInstance() {
		return inst == null ? inst = new OnlineEngine() : inst;
	}
	
	@Override
	public void enable() {
		setEnabled(true);
	}
	
	@Override
	public void disable() {
		setEnabled(false);
	}

	public static void removePlayer(Player player) {
		BlueLibPlayer p = online.remove(player.getUniqueId());
		LogEngine.debugMsg(LoggingLevel.DEBUG, OnlineEngine.class.getSimpleName(), " | Игрок ", p, " удалён из списка онлайна.");
	}

	public static void addPlayer(BlueLibPlayer player) {
		online.put(player.getUUID(), player);
		LogEngine.debugMsg(LoggingLevel.DEBUG, OnlineEngine.class.getSimpleName(), " | Игрок ", player, " добавлен в список онлайна.");
	}
	
	public static Map<UUID, BlueLibPlayer> getOnline() {
		return online;
	}
	
	public static void sendMessageToNearest(String msg) {
		online.forEach((uuid, p) -> p.sendMessage(msg));
	}
	
	public static void sendMessageForAll(String msg) {
		online.forEach((uuid, p) -> p.sendMessage(msg));
	}
	
	public static void broadcast(String msg) {
		Bukkit.broadcastMessage(msg);
	}
	
	public static void broadcast(String[] msgs, String... placeholders) {
		for (String msg : msgs) {
			broadcast(msg, placeholders);
		}
	}
	
	public static void broadcast(String msg, String... placeholders) {
		if (placeholders != null && placeholders.length % 2 == 0 && placeholders.length > 1) {
			for (int i = 0; i < placeholders.length; i+=2) {
				msg = msg.replace(placeholders[i], placeholders[i+1]);
			}
		}
		broadcast(msg);
	}
	
	public static void broadcast(String... msgs) {
		for (String msg : msgs) {
			broadcast(msg);
		}
	}
	
	/**
	 * @param sender игрок или консоль.
	 * @return возвращает объект sender'a для отправки сообщений.
	 */
	public static BlueLibSender getSender(CommandSender sender) {
		if (isConsoleSender(sender)) return CONSOLE_SENDER;
		Player p = (Player) sender;
		return online.get(p.getUniqueId());
	}
	
	/**
	 * @param name имя отправителя сообщений, может быть ник игрока или имя консоли.
	 * @return возвращает объект sender'a для отправки сообщений.
	 */
	public static BlueLibSender getSender(String name) {
		if (isConsoleSender(name)) return CONSOLE_SENDER;
		Player p = Bukkit.getPlayer(name);
		if (p == null) return null;
		return online.get(p.getUniqueId());
	}

	/**
	 * @param sender отправитель сообщений, может быть как консоль, так и игрок.
	 * @return возвращает объект-обёртку bukkit игрока с расширенным функционалом,
	 * если sender - консоль, то вернёт null.
	 */
	public static BlueLibPlayer getPlayer(CommandSender sender) {
		if (isConsoleSender(sender)) return null;
		return online.get(((Player) sender).getUniqueId());
	}

	/**
	 * @param player игрок.
	 * @return возвращает объект-обёртку bukkit игрока с расширенным функционалом,
	 * если sender - консоль, то вернёт null.
	 */
	public static BlueLibPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}
	
	/**
	 * @param playerName имя игрока, если не найдено, то вернёт null.
	 * @return возвращает объект-обёртку bukkit игрока с расширенным функционалом.
	 */
	public static BlueLibPlayer getPlayer(String playerName) {
		OfflinePlayer of = Bukkit.getOfflinePlayer(playerName);
		if (of.hasPlayedBefore()) {
			UUID uuid = of.getUniqueId();
			if (online.containsKey(uuid)) return online.get(uuid);
		}
		return null;
	}
	
	/**
	 * @param uuid уникальный ид игрока, если не найдено, то вернёт null.
	 * @return возвращает объект-обёртку bukkit игрока с расширенным функционалом.
	 */
	public static BlueLibPlayer getPlayer(UUID uuid) {
		return online.get(uuid);
	}
	
	public static void kickAll() {
		kickAll(Message.OnlineEngine_kickall.get());
	}
	
	public static void kickAll(String reason) {
		List<UUID> uuids = new ArrayList<>();
		online.forEach((uuid, o) -> uuids.add(uuid));

		for (UUID uuid : uuids) {
			BlueLibPlayer p = online.remove(uuid);
			kick(p, reason);
		}
	}
	
	public static void kick(BlueLibPlayer player, String reason) {
		player.getBukkitPlayer().kickPlayer(reason);
	}
	
	public static void kick(BlueLibPlayer player) {
		player.getBukkitPlayer().kickPlayer(Message.OnlineEngine_kick.get());
	}
	
	public static boolean isConsoleSender(CommandSender sender) {
		return isConsoleSender(sender.getName());
	}
	
	public static boolean isConsoleSender(String name) {
		return name.equalsIgnoreCase("CONSOLE");
	}
	
	public static void sendMessageToConsole(String message) {
		CONSOLE_SENDER.sendMessage(message);
	}
	
	public static void dispatchCommand(List<String> cmds) {
		cmds.forEach(OnlineEngine::dispatchCommand);
	}
	
	public static void dispatchCommand(String... cmds) {
		for (String cmd : cmds) {
			dispatchCommand(cmd);
		}
	}
	
	public static void dispatchCommand(String cmd) {
		if (cmd != null) {
			Bukkit.getServer().dispatchCommand(CONSOLE_SENDER.getCommandSender(), cmd);
		} else LogEngine.debugMsg(LoggingLevel.ERROR, OnlineEngine.class.getSimpleName(), " | Ошибка в выполнении команды, cmd: ", cmd);
	}
}

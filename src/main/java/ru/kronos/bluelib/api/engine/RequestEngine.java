package ru.kronos.bluelib.api.engine;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import ru.kronos.bluelib.Message;
import ru.kronos.bluelib.Setting;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.template.online.BlueLibSender;
import ru.kronos.bluelib.module.request.RequestCMD;
import ru.kronos.bluelib.module.request.RequestSystem;
import ru.kronos.bluelib.module.request.RequestTask;
import ru.kronos.bluelib.api.util.ThreadDaemon;

public final class RequestEngine extends BlueLibEngine {

	private static RequestEngine inst;

	private static final RequestSystem requestSystem = new RequestSystem();
	private static BukkitTask task;

	private RequestEngine() {}

	public static RequestEngine getInstance() {
		return inst == null ? inst = new RequestEngine() : inst;
	}
	
	@Override
	public void enable() {
		RequestCMD.newInstance();
		task = ThreadDaemon.asyncTimer(requestSystem, 0L, 20L * Setting.RequestEngine_clearTaskDelaySeconds.getLong());
		setEnabled(true);
	}
	
	@Override
	public void disable() {
		ThreadDaemon.cancelTask(task);
		RequestSystem.requests.clear();
		setEnabled(false);
	}
	
	private static void sendJsonMessage(String sender, String requestName, String... description) {
		BlueLibPlayer p = OnlineEngine.getPlayer(sender);
		Message.RequestEngine_jsonRequest
				.replace("{command}", "/bluelib-request ".concat(requestName))
				.replace("{description}", description.length == 0 ? Message.RequestEngine_defaultButtonTip.get() : description[0])
				.send(p);
	}
	
	/**
	 * Создаёт запрос с временем ожидания lifetime секунд. Запрос ожидает выполнения
	 * команды /bluelib-request requestName.
	 * @param requestName название действия.
	 * @param sender получатель.
	 * @param requestAction код, который выполнится после ответа на запрос.
	 * @param lifetime время жизни запроса в секундах.
	 * @param description описание на hoverEvent.
	 * @return вернёт true если запрос успешно создался.
	 */
	public static boolean createRequest(String requestName, BlueLibSender sender, Runnable requestAction, long lifetime, String... description) {
		return createRequest(requestName, sender.getName(), requestAction, lifetime, description);
	}
	
	/**
	 * Создаёт запрос с временем ожидания lifetime секунд. Запрос ожидает выполнения
	 * команды /bluelib-request requestName.
	 * @param requestName название действия.
	 * @param sender получатель.
	 * @param requestAction код, который выполнится после ответа на запрос.
	 * @param lifetime время жизни запроса в секундах.
	 * @param description описание на hoverEvent.
	 * @return вернёт true если запрос успешно создался.
	 */
	public static boolean createRequest(String requestName, CommandSender sender, Runnable requestAction, long lifetime, String... description) {
		return createRequest(requestName, sender.getName(), requestAction, lifetime, description);
	}
	
	/**
	 * Создаёт запрос с временем ожидания lifetime секунд. Запрос ожидает выполнения
	 * команды /bluelib-request requestName.
	 * @param requestName название действия.
	 * @param playerName получатель.
	 * @param requestAction код, который выполнится после ответа на запрос.
	 * @param lifetime время жизни запроса в секундах.
	 * @param description описание на hoverEvent.
	 * @return вернёт true если запрос успешно создался.
	 */
	public static boolean createRequest(String requestName, String playerName, Runnable requestAction, long lifetime, String... description) {
		String key = playerName.concat("-").concat(requestName);
		
		if (RequestSystem.requests.containsKey(key) && !RequestSystem.requests.get(key).ended()) return false;

		RequestSystem.requests.put(key, new RequestTask(requestAction, lifetime));

		sendJsonMessage(playerName, requestName, description);

		return true;
	}
}

package ru.kronos.bluelib.license;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class License {
	
	/*
	 * Формат лицензии
	 * $ProjectName|$MainClass|$ip1,$ip2|$ttl1|$ttl2
	 * $ProjectName   = Название проекта, для которого создаётся плагин.
	 * $MainClass     = Главный класс из plugin.yml.
	 * $ip1,$ip2, ... = IP адреса, на которых работает плагин.
	 * $ttl           = HH:mm dd.MM.yyyy.
	 * 
	 * Проверка лицензии должна проходить на этапе регистрации плагина (PluginManager).
	 */
	
	private String projectName;
	private String mainClass;
	private String[] ips;
	private final Date[] ttl = new Date[2];
	private boolean corrupted = false;
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
	
	public License(byte[] encryptedLicense, BlueLibPlugin plugin) {
		try {
			String decryptedText = CryptoEngine.decrypt(encryptedLicense);
			
			String[] parts = decryptedText.split("\\|");

			this.projectName = parts[0];
			this.mainClass = parts[1];
			this.ips = parts[2].split(",");
			this.ttl[0] = sdf.parse(parts[3]);
			this.ttl[1] = sdf.parse(parts[4]);
			
		} catch (NullPointerException | ParseException e) {
			this.corrupted = true;
			LogEngine.debugMsg(LoggingLevel.MINIMUM, License.class.getSimpleName(), " | При чтении лицензии плагина ", plugin.getPlugin().getName(), " произошла непредвиденная ошибка.");
			e.printStackTrace();
		}
	}
	
	public boolean isValid(BlueLibPlugin plugin) {
		if (corrupted) return false;

		LogEngine.debugMsg(LoggingLevel.INFO, "§5Лицензия:");
		LogEngine.debugMsg(LoggingLevel.INFO, "§5 - ID: §f", plugin.getPluginID().toString());
		LogEngine.debugMsg(LoggingLevel.INFO, "§5 - Главный класс: §f", mainClass);
		LogEngine.debugMsg(LoggingLevel.INFO, "§5 - IPs: [ §f", String.join("§5, §f", ips), " §5]");
		LogEngine.debugMsg(LoggingLevel.INFO, "§5 - Действительна с §f", sdf.format(ttl[0]), " §5по §f", sdf.format(ttl[1]));
		LogEngine.debugMsg(LoggingLevel.INFO, "§5 - Для проекта: §f", projectName);
		
		JavaPlugin jp = plugin.getPlugin();

		if (!Main.projectName.equalsIgnoreCase(projectName)) {
			return false;
		}

		if (!jp.getClass().getCanonicalName().equals(mainClass)) {
			return false;
		}

		if (System.currentTimeMillis() < ttl[0].getTime() || ttl[1].getTime() < System.currentTimeMillis()) {
			return false;
		}
		
		String sip = Bukkit.getServer().getIp();
//		try (final DatagramSocket socket = new DatagramSocket()){
//			  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
//			  sip = socket.getLocalAddress().getHostAddress();
//		} catch (SocketException | UnknownHostException e) {
//			e.printStackTrace();
//			return false;
//		}
		
		ok: {
			for (String ip : ips) {
				if (ip.equalsIgnoreCase("any") || ip.equalsIgnoreCase("localhost") || ip.equalsIgnoreCase("127.0.0.1") || sip.startsWith("192.168"))
					break ok;
				if (ip.equalsIgnoreCase(sip)) break ok;
			}

			return false;
		}

		return true;
	}
}

package ru.kronos.chatassistant;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.api.util.Utils;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.chatassistant.channel.ChatChannel;
import ru.kronos.chatassistant.scheme.GroupScheme;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatEngine extends BlueLibEngine {

	private static ChatEngine inst;

	private ChatEngine() {}

	public static ChatEngine getInstance() {
		return inst == null ? inst = new ChatEngine() : inst;
	}

	private static Pattern linkRegex;
	private static String linkFormatJson;
	private static TextComponent spyPrefix;
	private static GroupScheme[] groupSchemes;

	@Override
	public void disable() {
		ChatElement.clear();
		linkRegex = null;
		linkFormatJson = null;
		groupSchemes = null;
		spyPrefix = null;
	}

	@Override
	public void enable() {
		ConfigurationSection c = Main.config.get();
		Setting.load(c);

		// Регулярное выражение для поиска ссылки в сообщении
		if (Setting.TranslateLink.getBool()) {
			linkRegex = Pattern.compile(Setting.LinkRegex.getString());
		}

		// Вид ссылки для чата в формате json
		linkFormatJson = ComponentSerializer.toString(Utils.createTextComponent(c.getConfigurationSection("LinkFormat")));

		// Префикс чата при прослушке
		spyPrefix = Utils.createTextComponent(c.getConfigurationSection("SpyPrefix"));

		// Инициализация каналов чата
		reloadChatChannels(c);

		// Инициализация групповых схем и приоритетов
		reloadGroupSchemes(c);

		// Элементы чата (элементные блоки сообщения)
		reloadChatElements(c);

		LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Групповых схем загружено: ", groupSchemes.length);
		LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Элементов чата загружено: ", ChatElement.count());
	}

	private static void reloadChatChannels(ConfigurationSection c) {
		LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Загрузка каналов чата... ");

		if (c.contains("ChatChannels")) {
			ConfigurationSection chatChannels = c.getConfigurationSection("ChatChannels");

			for (String channelName : chatChannels.getKeys(false)) {
				ChatChannel ch = ChatChannel.getChatChannel(channelName);

				if (ch != null) {
					ch.reloadByConfig(chatChannels.getConfigurationSection(channelName));
					LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(), " | Инициализирован канал чата ", ch.NAME);
				} else {
					LogEngine.debugMsg(LoggingLevel.ERROR, Main.inst.getName(), " | Канал ", channelName, " не найден!");
				}
			}
		}
	}

	private static void reloadGroupSchemes(ConfigurationSection c) {
		LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Загрузка групповых чат-схем... ");

		List<String> priorities = c.getStringList("GroupPriority");
		groupSchemes = new GroupScheme[priorities.size() + 1];

		for (int i = 0; i < groupSchemes.length - 1; i++) {
			String groupName = priorities.get(i);
			groupSchemes[i] = new GroupScheme(groupName, c.getConfigurationSection("GroupSchemes." + groupName));
			LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(),
					" | Инициализирована чат-схема для группы ", groupName, " с приоритетом: ", i);
		}

		groupSchemes[groupSchemes.length - 1] = new GroupScheme("general", c.getConfigurationSection("GeneralScheme"));
		LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(),
				" | Добавлена базовая чат-схема general с приоритетом: ", groupSchemes.length - 1);
	}

	private static void reloadChatElements(ConfigurationSection c) {
		LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Загрузка элементов чата... ");

		for (String elementName : c.getConfigurationSection("ChatElements").getKeys(false)) {
			ChatElement.createChatElement(elementName)
					.reloadByConfig(c.getConfigurationSection("ChatElements." + elementName))
					.register();
		}
	}

	public static GroupScheme[] getGroupSchemes() {
		return groupSchemes;
	}

	public static String getLinkFormatJson() {
		return linkFormatJson;
	}

	public static Pattern getLinkRegex() {
		return linkRegex;
	}

	public static TextComponent getSpyPrefix() {
		return spyPrefix.duplicate();
	}

	/**
	 * Сокращает ссылки в сообщении
	 */
	public static List<TextComponent> shortLinkByRegex(String sourceMessage) {
		List<TextComponent> comps = new ArrayList<>();

		Matcher m = ChatEngine.getLinkRegex().matcher(sourceMessage);

		int end = 0;
		// Достаём первый Full match, если он есть
		if (m.find()) {
			// Записываем часть сообщения до Full match, если она есть
			if (m.start() != 0) comps.add(new TextComponent(sourceMessage.substring(0, m.start())));
			do {
				// Не работает на первом шаге
				// Если следующая ссылка в строке стоит на каком-то
				// расстоянии от предыдущей, то записываем этот отступ
				if (end != 0 && end < m.start()) {
					comps.add(new TextComponent(sourceMessage.substring(end , m.start())));
				}
				// Достаём ссылку из строки
				comps.add(createLink(sourceMessage.substring(m.start(), m.end())));

				end = m.end();
			} while (m.find());
			// Если в строки конец последней ссылки не последний символ в
			// строке, то дописываем "хвост".
			if (end < sourceMessage.length()) comps.add(new TextComponent(sourceMessage.substring(end)));
		} else comps.add(new TextComponent(sourceMessage));

		return comps;
	}

	private static TextComponent createLink(String link) {
		return new TextComponent(
				ComponentSerializer.parse(
						ChatEngine.getLinkFormatJson().replace("{link}", link)
				)
		);
	}
}

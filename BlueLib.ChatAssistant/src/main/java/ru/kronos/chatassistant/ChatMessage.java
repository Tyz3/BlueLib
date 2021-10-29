package ru.kronos.chatassistant;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import ru.kronos.bluelib.Message;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.chatassistant.channel.ChatChannel;
import ru.kronos.chatassistant.scheme.GroupScheme;

import java.util.List;
import java.util.Set;

public final class ChatMessage {

    private final ChatChannel channel;
    private final String sourceMessage;
    private final BlueLibPlayer sender;
    private Set<Player> recipients;
    private Set<Player> spyRecipients;

    // Кэш для сборки сообщений
    private TextComponent[] messageBySchemes;
    private TextComponent[] spyMessageBySchemes;
    private boolean builded = false;

    public ChatMessage(ChatChannel channel, String sourceMessage, BlueLibPlayer sender, Set<Player> recipients) {
        this.channel = channel;
        this.sourceMessage = sourceMessage;
        this.sender = sender;
        this.recipients = recipients;
    }

    public ChatMessage build() {
        // Бывает такое, что при входе на сервер данные игрока ещё не загрузились, если
        // загрузка идёт с жёсткого диска
        // Благо есть система кэширования, чтобы встречать подобное никогда
        if (!sender.isLoaded()) {
            // Сообщение игроку, что его данные ещё не загружены
            Message.StorageEngine_loadingData.send(sender);
            return this;
        }

        spyRecipients = channel.getSpyRecipients(sender, recipients);
        recipients = channel.getRecipients(sender, recipients);

        if (recipients.isEmpty()) return this;

        // Если в конфиге указан глобальный формат сообщения, то сообщение будет одинаковым для всех получателей
        if (channel.hasMessageScheme()) {
            List<TextComponent> message = sourceMessageToTextComponents(sourceMessage, false);

            TextComponent comp = channel.getMessageScheme().createFinalComponent(sender, channel, message);

            // Кэширование собранного сообщения
            messageBySchemes = new TextComponent[] {comp};

            // Кэширование сообщения для шпионов
            TextComponent spyComp = ChatEngine.getSpyPrefix();
            spyComp.addExtra(comp);
            spyMessageBySchemes = new TextComponent[] {spyComp};
        } else {
            List<TextComponent> message = sourceMessageToTextComponents(sourceMessage, true);

            // Создадим сообщения под все существующие роли
            GroupScheme[] schemes = ChatEngine.getGroupSchemes();
            messageBySchemes = new TextComponent[schemes.length];
            spyMessageBySchemes = new TextComponent[schemes.length];

            for (int i = 0; i < schemes.length; i++) {
                TextComponent comp = schemes[i].SCHEME.createFinalComponent(sender, channel, message);

                // Кэширование собранного сообщения под определённым индексом
                messageBySchemes[i] = comp;

                // Кэширование сообщения для шпионов
                TextComponent spyComp = ChatEngine.getSpyPrefix();
                spyComp.addExtra(comp);
                spyMessageBySchemes[i] = spyComp;
            }
        }

        builded = true;
        return this;
    }

    public void send() {
        if (!builded) return;

        GroupScheme[] schemes = ChatEngine.getGroupSchemes();
        // Отправка сообщения получателям в соотствии с их разрешениями
        for (Player r : recipients) {

            // Отправка шаблонных сообщений, кроме последнего
            for (int i = 0; i < schemes.length; i++) {
                // Количество схем всегда больше 0, так как последней схемой добавляется базовая схема,
                // по которой сообщение отправляется в любом случае
                if (messageBySchemes.length == 1 || i == schemes.length - 1) {
                    r.spigot().sendMessage(messageBySchemes[i]);
                    break;
                }
            }
        }

        // Отправка сообщения шпионам в соотствии с их разрешениями
        for (Player r : spyRecipients) {

            // Отправка шаблонных сообщений, кроме последнего
            for (int i = 0; i < schemes.length; i++) {
                // Количество схем всегда больше 0, так как последней схемой добавляется базовая схема,
                // по которой сообщение отправляется в любом случае
                if (spyMessageBySchemes.length == 1 || i == schemes.length - 1) {
                    r.spigot().sendMessage(spyMessageBySchemes[i]);
                    break;
                }
            }
        }

        // Отправка сообщения в дискорд канал
        if (channel.hasDiscordTextChannel()) {
            channel.sendMessageToDiscord(
                    Setting.DiscordSRV_messageFormat.getString()
                            .replace("{channelName}", channel.NAME)
                            .replace("{displayName}", sender.getDisplayName())
                            .replace("{message}", sourceMessage)
            );
        }
    }

    private static List<TextComponent> sourceMessageToTextComponents(String sourceMessage, boolean shortLink) {
        // Нужно ли сократить ссылку в сообщении
        return Setting.TranslateLink.getBool() && shortLink ?
                ChatEngine.shortLinkByRegex(sourceMessage) : List.of(new TextComponent(sourceMessage));
    }
}

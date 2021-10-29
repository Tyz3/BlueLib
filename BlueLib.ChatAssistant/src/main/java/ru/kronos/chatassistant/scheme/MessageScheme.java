package ru.kronos.chatassistant.scheme;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.engine.API3rdPartyEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.Placeholder;
import ru.kronos.bluelib.api.util.Utils;
import ru.kronos.chatassistant.ChatElement;
import ru.kronos.chatassistant.Setting;
import ru.kronos.chatassistant.channel.ChatChannel;

import java.util.List;

public class MessageScheme {

    public final String SCHEME;
    public final ChatColor DISPLAY_NAME_COLOR;
    public final ChatColor MESSAGE_COLOR;

    public MessageScheme(ConfigurationSection c) {
        SCHEME = c.getString("scheme", Setting.GeneralSetting_scheme.getString());

        String dispColor = c.getString("displayName", Setting.GeneralSetting_displayName.getString());
        DISPLAY_NAME_COLOR = ChatColor.of(dispColor);

        String msgColor = c.getString("message", Setting.GeneralSetting_message.getString());
        MESSAGE_COLOR = ChatColor.of(msgColor);
    }

    public TextComponent createFinalComponent(BlueLibPlayer sender, ChatChannel channel, List<TextComponent> message) {
        TextComponent comp = new TextComponent();

        // Разбивка схемы на элементы
        for (String element : SCHEME.split(" ")) {
            if (!ChatElement.hasChatElement(element)) continue;

            ChatElement chatElement = ChatElement.getChatElement(element);

            // Хард-кодные подстановки
            if (chatElement == ChatElement.CH_PRFX)
            {
                if (channel.hasChannelPrefix()) {
                    comp.addExtra(channel.getChannelPrefix());
                }
            }
            else if (chatElement == ChatElement.PRFX)
            {
                if (sender.hasPrefix()) {
                    comp.addExtra(chatElement.getComponent());
                }
            }
            else if (chatElement == ChatElement.DISP)
            {
                TextComponent tc = chatElement.getComponent();
                if (!tc.hasFormatting()) {
                    tc.setColor(DISPLAY_NAME_COLOR);
                }
                comp.addExtra(tc);
            }
            else if (chatElement == ChatElement.SUFF)
            {
                if (sender.hasSuffix()) {
                    comp.addExtra(chatElement.getComponent());
                }
            }
            else if (chatElement == ChatElement.MESG)
            {
                for (TextComponent msg : message) {
                    if (channel.hasMessageColor()) {
                        msg.setColor(channel.getMessageColor());
                    } else {
                        msg.setColor(MESSAGE_COLOR);
                    }

                    comp.addExtra(msg);
                }
            }
            else if (chatElement == ChatElement.DSRV_LNK)
            {
                if (sender.hasDiscordId()) {
                    comp.addExtra(chatElement.getComponent());
                }
            }
            else if (chatElement == ChatElement.DSRV_ULNK)
            {
                if (!sender.hasDiscordId()) {
                    comp.addExtra(chatElement.getComponent());
                }
            }
            else if (chatElement != null)
            {
                comp.addExtra(chatElement.getComponent());
            }
        }

        // Заполнение полей с подстановками: {prefix}, {displayName}, ...
        String json = ComponentSerializer.toString(comp);
        BaseComponent[] bcomps = ComponentSerializer.parse(setPlaceholders(sender, json));

        // Финальная сборка сообщения
        comp = Utils.collectComponents(bcomps);

        return comp;
    }

    private static String setPlaceholders(BlueLibPlayer sender, String json) {

        if (Setting.PlaceHolderAPI_Enabled.getBool() && API3rdPartyEngine.enabledPlaceholderAPI()) {
            json = PlaceholderAPI.setPlaceholders(sender.getBukkitPlayer(), json);
        }

        return Placeholder.set(json,
                "{prefix}", sender.getPrefix(),
                "{displayName}", sender.getDisplayName(),
                "{name}", sender.getName(),
                "{suffix}", sender.getSuffix(),
//                "{time}", String.valueOf(System.currentTimeMillis() - t),
                "{ds-tag}", sender.hasDiscordId() ? sender.getDiscordTag() : "",
                "\\\\u", "\\u"
        );
    }
}

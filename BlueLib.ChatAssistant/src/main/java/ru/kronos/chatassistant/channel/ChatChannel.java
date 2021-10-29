package ru.kronos.chatassistant.channel;

import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.kronos.bluelib.api.engine.API3rdPartyEngine;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.bluelib.api.util.Utils;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.chatassistant.Main;
import ru.kronos.chatassistant.scheme.MessageScheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class ChatChannel {

    private static final List<ChatChannel> CHAT_CHANNELS = new ArrayList<>();

    public static final ChatChannel SHOUT =         new ChatChannel("SHOUT");
    public static final ChatChannel LOCAL =         new ChatChannel("LOCAL");
    public static final ChatChannel TRADE =         new ChatChannel("TRADE");
    public static final ChatChannel HELP =          new ChatChannel("HELP");
    public static final ChatChannel DO =            new ChatChannel("DO");
    public static final ChatChannel ME =            new ChatChannel("ME");
    public static final ChatChannel NONRP =         new ChatChannel("NONRP");
    public static final ChatChannel SCREAM =        new ChatChannel("SCREAM");
    public static final ChatChannel WHISPER =       new ChatChannel("WHISPER");
    public static final ChatChannel BROADCAST =     new ChatChannel("BROADCAST");

    public final String NAME;

    private final String SEE_PERMISSION;
    private final String SPY_PERMISSION;

    private MessageScheme messageScheme;
    private TextComponent chatPrefix;
    private ChatColor messageColor;
    private TextChannel discordTextChannel;
    private double range;

    private ChatChannel(String name) {
        NAME = name;
        CHAT_CHANNELS.add(this);
        SEE_PERMISSION = "chatassistant.chatgroup.".concat(name.toLowerCase()).concat(".see");
        SPY_PERMISSION = "chatassistant.chatgroup.".concat(name.toLowerCase()).concat(".spy");
    }

    public Set<Player> getRecipients(BlueLibPlayer sender, Set<Player> recipients) {
        return recipients.stream()
                .filter(r -> canSee(r) || r == sender.getBukkitPlayer())
                .filter(r -> isGlobal() || MathOperation.distance3D(r.getLocation(), sender.getLocation()) <= getRange())
                .collect(Collectors.toSet());
    }

    public Set<Player> getSpyRecipients(BlueLibPlayer sender, Set<Player> recipients) {
        return recipients.stream().filter(r -> canSpy(r) && r != sender.getBukkitPlayer()).collect(Collectors.toSet());
    }

    public void reloadByConfig(ConfigurationSection c) {
        messageScheme = null;
        chatPrefix = null;
        messageColor = null;
        discordTextChannel = null;

        if (c.contains("Message")) {
            messageScheme = new MessageScheme(c.getConfigurationSection("Message"));
        }

        if (c.contains("Prefix")) {
            chatPrefix = Utils.createTextComponent(c.getConfigurationSection("Prefix"));
        }

        if (c.contains("messageColor")) {
            messageColor = ChatColor.of(c.getString("messageColor"));
        }

        range = c.getDouble("range", 0D);

        if (API3rdPartyEngine.enabledDiscordSRV()) {
            if (c.contains("id")) {
                String id = c.getString("id", "");

                if (id.length() != 0) {
                    discordTextChannel = DiscordUtil.getTextChannelById(id);

                    if (discordTextChannel != null) {
                        LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Для канала ", this,
                                " настроен канал дискорда: ", discordTextChannel);
                    }
                }
            }
        }
    }

    public boolean hasMessageScheme() {
        return messageScheme != null;
    }

    public MessageScheme getMessageScheme() {
        return messageScheme;
    }

    public boolean hasChannelPrefix() {
        return chatPrefix != null;
    }

    public TextComponent getChannelPrefix() {
        return chatPrefix.duplicate();
    }

    public boolean hasMessageColor() {
        return messageColor != null;
    }

    public ChatColor getMessageColor() {
        return messageColor;
    }

    public boolean isGlobal() {
        return range == 0D;
    }

    public double getRange() {
        return range;
    }

    public boolean hasDiscordTextChannel() {
        return discordTextChannel != null;
    }

    public void sendMessageToDiscord(String message) {
        discordTextChannel.sendMessage(message).queue();
    }

    public boolean canSee(Player player) {
        return player.hasPermission(SEE_PERMISSION);
    }

    public boolean canSpy(Player player) {
        return player.hasPermission(SPY_PERMISSION);
    }

    public static ChatChannel getChatChannel(String name) {
        for (ChatChannel c : CHAT_CHANNELS) {
            if (c.NAME.equalsIgnoreCase(name)) {
                return c;
            }
        }

        return null;
    }
}

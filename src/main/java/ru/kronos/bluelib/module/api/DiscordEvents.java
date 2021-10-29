package ru.kronos.bluelib.module.api;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.AccountLinkedEvent;
import github.scarsz.discordsrv.api.events.AccountUnlinkedEvent;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import org.bukkit.OfflinePlayer;
import ru.kronos.bluelib.Message;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.extra.LoggingLevel;

public class DiscordEvents {

    private static DiscordEvents inst;

    private DiscordEvents() {
        DiscordSRV.api.subscribe(this);
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new DiscordEvents();
        }
    }

    public static void sendDiscordGuildMessage(String channel, String message) {
        DiscordUtil.sendMessage(DiscordUtil.getTextChannelById(channel), message);
    }

    @Subscribe
    public void accountsLinked(AccountLinkedEvent event) {
        OfflinePlayer player = event.getPlayer();

        if (event.getPlayer().isOnline()) {
            BlueLibPlayer p = OnlineEngine.getPlayer(player.getUniqueId());
            p.setDiscordId(event.getUser().getId());
        }

        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("linking");

        if (textChannel != null) {
            String msg = Message.API3rdPartyEngine_DiscordSRV_linkedAccount
                    .replace("{player}", player.getName())
                    .replace("{uuid}", player.getUniqueId().toString())
                    // TODO Исправить, подстановка на null
                    .replace("{discordTag}", event.getUser() != null ? event.getUser().getAsTag() : "null")
                    .replace("{discordUserId}", event.getUser() != null ? event.getUser().getId() : "null")
                    .get();

            textChannel.sendMessage(msg).queue();
        } else {
            LogEngine.debugMsg(LoggingLevel.ERROR, "DiscordSRV | Канал \"linking\" не найден в конфигурации DiscordSRV.");
        }
    }

    @Subscribe
    public void accountUnlinked(AccountUnlinkedEvent event) {
        OfflinePlayer player = event.getPlayer();

        if (player.isOnline()) {
            BlueLibPlayer p = OnlineEngine.getPlayer(player.getUniqueId());
            p.setDiscordId(null);
        }

        // Example of DM:ing user on unlink
        //DiscordUtil.getJda().getUserById(event.getDiscordId());
        github.scarsz.discordsrv.dependencies.jda.api.entities.User user = event.getDiscordUser();

        // will be null if the bot isn't in a Discord server with the user (eg. they left the main Discord server)
        if (user != null) {

            // opens/retrieves the private channel for the user & sends a message to it (if retrieving the private channel was successful)
            String msg = Message.API3rdPartyEngine_DiscordSRV_unlinkedAccountPM
                    .replace("{plyaer}", player.getName())
                    .get();

            user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(msg).queue());
        }

        // Example of sending a message to a channel called "unlinks" (defined in the config.yml using the Channels option) when a user unlinks
        TextChannel textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("linking");

        // null if the channel isn't specified in the config.yml
        if (textChannel != null) {

            String msg = Message.API3rdPartyEngine_DiscordSRV_unlinkedAccount
                    .replace("{player}", player.getName())
                    .replace("{uuid}", player.getUniqueId().toString())
                    .replace("{discordTag}", event.getDiscordUser().getAsTag())
                    .replace("{discordUserId}", event.getDiscordId())
                    .get();

            textChannel.sendMessage(msg).queue();
        } else {
            LogEngine.debugMsg(LoggingLevel.ERROR,
                    "DiscordSRV | Не удалось отправить сообщение об отвязке аккаунта ",	player.getName(),
                    ": канал с названием \"linking\" не найден в привязанном Discord-канале.");
        }
    }

}

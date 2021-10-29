package ru.kronos.chatassistant.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.chatassistant.ChatMessage;
import ru.kronos.chatassistant.Main;
import ru.kronos.chatassistant.channel.ChatChannel;

import java.util.List;
import java.util.stream.Collectors;

public final class ScreamCMD extends BlueLibCommand {

    private static ScreamCMD inst;

    private ScreamCMD() {
        super(Main.inst, "scream");
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new ScreamCMD();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length > 0) {
            new ChatMessage(
                    ChatChannel.SCREAM, String.join(" ", args), OnlineEngine.getPlayer(sender),
                    OnlineEngine.getOnline().values().stream().map(BlueLibPlayer::getBukkitPlayer)
                            .collect(Collectors.toSet())
            ).build().send();

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}

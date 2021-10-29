package ru.kronos.chatassistant.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.chatassistant.ChatMessage;
import ru.kronos.chatassistant.Main;
import ru.kronos.chatassistant.channel.ChatChannel;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class WhisperCMD extends BlueLibCommand {

    private static WhisperCMD inst;

    private WhisperCMD() {
        super(Main.inst, "whisper");
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new WhisperCMD();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length > 0) {
            new ChatMessage(
                    ChatChannel.WHISPER, String.join(" ", args), OnlineEngine.getPlayer(sender),
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

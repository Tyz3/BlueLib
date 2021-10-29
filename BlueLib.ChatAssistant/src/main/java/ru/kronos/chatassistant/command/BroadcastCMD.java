package ru.kronos.chatassistant.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.api.engine.CooldownEngine;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.chatassistant.ChatMessage;
import ru.kronos.chatassistant.Main;
import ru.kronos.chatassistant.channel.ChatChannel;

import java.util.List;
import java.util.stream.Collectors;

public final class BroadcastCMD extends BlueLibCommand {

    private static BroadcastCMD inst;

    private BroadcastCMD() {
        super(Main.inst, "broadcast");
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new BroadcastCMD();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length > 0) {

            if (CooldownEngine.hasCooldown(Main.inst.getPluginID(), sender, "broadcast")) {
                sender.sendMessage("§7Следующее использование команды возможно через §2"
                        .concat(String.valueOf(CooldownEngine.getCooldown(Main.inst.getPluginID(), sender, "broadcast")))
                        .concat("§7 сек."));
                return true;
            }

            new ChatMessage(
                    ChatChannel.BROADCAST, String.join(" ", args), OnlineEngine.getPlayer(sender),
                    OnlineEngine.getOnline().values().stream().map(BlueLibPlayer::getBukkitPlayer)
                            .collect(Collectors.toSet())
            ).build().send();

            CooldownEngine.setCooldown(Main.inst.getPluginID(), "broadcast", sender, 300L);

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}

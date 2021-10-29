package ru.kronos.chatassistant.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.chatassistant.Main;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TodoCMD extends BlueLibCommand {

    private static TodoCMD inst;

    private TodoCMD() {
        super(Main.inst, "todo");
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new TodoCMD();
        }
    }

    private static final String MSG_FORMAT = "§f{part1}, — сказал §5{playerName}, {part2}";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length > 0) {
            String sourceMessage = String.join(" ", args);
            if (!sourceMessage.contains("@")) return false;

            String[] parts = sourceMessage.split("@");
            if (parts.length != 2) return false;

            BlueLibPlayer player = OnlineEngine.getPlayer(sender);
            String message = MSG_FORMAT
                    .replace("{part1}", parts[0].trim())
                    .replace("{playerName}", player.getDisplayName())
                    .replace("{part2}", parts[1].trim());
            String spyMessage = "§c§l[SPY] ".concat(message);

            Set<Player> rec = OnlineEngine.getOnline().values().stream().map(BlueLibPlayer::getBukkitPlayer)
                    .collect(Collectors.toSet());

            Set<Player> recipients = getRecipients(player, rec);
            Set<Player> spyRecipients = getSpyRecipients(player, rec);

            recipients.forEach(r -> r.sendMessage(message));
            spyRecipients.forEach(r -> r.sendMessage(spyMessage));

            return true;
        }

        return false;
    }

    private static Set<Player> getRecipients(BlueLibPlayer sender, Set<Player> recipients) {
        return recipients.stream()
                .filter(r -> r.hasPermission("chatassistant.todo.see") || r == sender.getBukkitPlayer())
                .filter(r -> MathOperation.distance3D(r.getLocation(), sender.getLocation()) <= 15D)
                .collect(Collectors.toSet());
    }

    private static Set<Player> getSpyRecipients(BlueLibPlayer sender, Set<Player> recipients) {
        return recipients.stream()
                .filter(r -> r.hasPermission("chatassistant.todo.spy") && r != sender.getBukkitPlayer())
                .collect(Collectors.toSet());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}

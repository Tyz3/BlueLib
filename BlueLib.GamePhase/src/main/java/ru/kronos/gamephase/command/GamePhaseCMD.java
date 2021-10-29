package ru.kronos.gamephase.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.CompleteHelper;
import ru.kronos.gamephase.GamePhase;
import ru.kronos.gamephase.GamePhaseEngine;
import ru.kronos.gamephase.Main;

import java.util.List;

public class GamePhaseCMD extends BlueLibCommand {

    private static GamePhaseCMD inst;

    private GamePhaseCMD() {
        super(Main.inst, "gamephase");
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new GamePhaseCMD();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
            String playerName = args[0];
            String gamePhaseName = args[2];

            BlueLibPlayer player = OnlineEngine.getPlayer(playerName);
            if (player == null) {
                sender.sendMessage("§cИгрок §4" + playerName + "§с не найден (не в сети).");
                return true;
            }

            GamePhase gamePhase = GamePhaseEngine.getGamePhaseByName(gamePhaseName);
            if (gamePhase == null) {
                sender.sendMessage("§cИгровая фаза §4" + gamePhaseName + "§с не существует.");
                return true;
            }

            GamePhaseEngine.changePlayerGamePhase(player, gamePhase);
            sender.sendMessage("§7Игровая фаза §f" + gamePhaseName + "§7 установлена игроку §a" + playerName + "§7.");
            return true;
        }

        if (args.length == 2 && args[1].equalsIgnoreCase("reset")) {
            String playerName = args[0];

            BlueLibPlayer player = OnlineEngine.getPlayer(playerName);
            if (player == null) {
                sender.sendMessage("§cИгрок §4" + playerName + "§с не найден (не в сети).");
                return true;
            }

            GamePhaseEngine.clearPlayerGamePhase(player);
            sender.sendMessage("§7Выполнен сброс игровой фазы у §a" + playerName + "§7, установлена фаза §fdefault§7.");
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission("gamephase.reload")) {
                Main.inst.disable();
                Main.inst.enable();
                sender.sendMessage("§aПлагин §2" + Main.inst.getName() + "§a успешно перезагружен.");
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            return CompleteHelper.filter(args,
                    OnlineEngine.getOnline().values().stream().map(BlueLibPlayer::getName).toArray(String[]::new));
        }

        if (args.length == 2) {
            return CompleteHelper.filter(args, "set", "reset");
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
            return CompleteHelper.filter(args,
                    GamePhaseEngine.GAME_PHASES.values().stream().map(GamePhase::getName).toArray(String[]::new));
        }

        return null;
    }
}

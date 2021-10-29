package ru.kronos.chestcommands.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.chestcommands.CCEngine;
import ru.kronos.chestcommands.Main;

import java.util.List;

public class ItemDatabaseCMD extends BlueLibCommand {

    private static ItemDatabaseCMD inst;

    private ItemDatabaseCMD() {
        super(Main.inst, "itemdatabase");
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new ItemDatabaseCMD();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (OnlineEngine.isConsoleSender(sender)) return false;

        if (args.length == 0) {
            CCEngine.getInstance().getItemDB().openPage(OnlineEngine.getPlayer(sender), 1);
            return true;
        }

        if (args.length == 1) {
            int page = Integer.parseInt(args[0]);

            CCEngine.getInstance().getItemDB().openPage(OnlineEngine.getPlayer(sender), page);
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return null;
    }
}

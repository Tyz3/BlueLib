package ru.kronos.resourcepacks.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.resourcepacks.Main;
import ru.kronos.resourcepacks.RPEngine;

import java.util.List;

public class RemoveResourcePackCMD extends BlueLibCommand {

    private static RemoveResourcePackCMD inst;

    private RemoveResourcePackCMD() {
        super(Main.inst, "remove-resourcepack");
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new RemoveResourcePackCMD();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (RPEngine.getInstance().removeResourcePack()) {
            sender.sendMessage("§aРесурс-пак удалён.");
        } else {
            sender.sendMessage("§cРесурс-пак не был удалён.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

//        if (args.length == 1) {
//            return CompleteHelper.filter(
//                    args, RPEngine.RESOURCE_PACKS.stream().map(ResourcePack::getName).toArray(String[]::new)
//            );
//        }

        return null;
    }
}

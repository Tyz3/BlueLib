package ru.kronos.resourcepacks.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import ru.kronos.bluelib.api.template.BlueLibCommand;
import ru.kronos.bluelib.api.util.CompleteHelper;
import ru.kronos.resourcepacks.Main;
import ru.kronos.resourcepacks.RPEngine;

import java.util.List;

public class UpdateResourcePackCMD extends BlueLibCommand {

    private static UpdateResourcePackCMD inst;

    private UpdateResourcePackCMD() {
        super(Main.inst, "update-resourcepack");
    }

    public static void newInstance() {
        if (inst == null) {
            inst = new UpdateResourcePackCMD();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 2) {

            String hash = args[0].toLowerCase();
            String uri = args[1];

            if (RPEngine.validateHash(hash)) {
                sender.sendMessage("§7Указана неправильная хэш-сумма (§cдлина != 40 символов§7): §8" + hash + "§7.");
                return true;
            }

            if (RPEngine.validateUri(uri)) {
                sender.sendMessage("§7Указана неверная ссылка: §c" + uri + "§7.");
                return true;
            }

            if (RPEngine.getInstance().updateResourcePack(hash, uri)) {
                sender.sendMessage("§aСписок ресурс-паков успешно обновлён.");
            } else {
                sender.sendMessage("§cРесурс-пак не был обновлён.");
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
//            String[] tips = RPEngine.RESOURCE_PACKS.stream().map(ResourcePack::getName).toArray(String[]::new);
//
//            return CompleteHelper.filter(
//                    args, tips.length == 0 ? new String[]{"название ресурс-пака до 7 символов"} : tips
//            );
            return CompleteHelper.filter(args, "хэш сумма SHA1 zip-архива");
        }

        if (args.length == 2) {
            return CompleteHelper.filter(args, "прямая ссылка (URI) на ресурс-пак");
        }

        return null;
    }
}

package ru.kronos.chatassistant;

import org.bukkit.command.CommandSender;

public enum Permission {

    USE_SHOUT_CHAT("chatassistant.shout.use", "&cУ вас нет нужных прав <permission>."),
    USE_TRADE_CHAT("chatassistant.trade.use", "&cУ вас нет нужных прав <permission>."),
    USE_HELP_CHAT("chatassistant.help.use", "&cУ вас нет нужных прав <permission>.")

    ;

    private final String perm;
    private final String message;

    Permission(String perm, String message) {
        this.perm = perm;
        this.message = message.replace("<permission>", perm).replace("&", "§");
    }

    public boolean has(CommandSender sender) {
        return sender.hasPermission(perm);
    }

    public void sendHasntMessage(CommandSender sender) {
        if (message.length() != 0) sender.sendMessage(message);
    }

}

package ru.kronos.resourcepacks;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.extra.LoggingLevel;

import java.util.regex.Pattern;

public class RPEngine extends BlueLibEngine implements Listener {

    private static RPEngine inst;

    public static final Pattern LINK_REGEX = Pattern.compile("([\\w]+://)?([\\d\\w]+\\.)+[\\d\\w]{2,}(/[\\w\\d\\-?&=#.]+)*");
    private static ResourcePack resourcePack;

    private RPEngine() {
        Bukkit.getPluginManager().registerEvents(this, Main.inst);
    }

    public static RPEngine getInstance() {
        return inst == null ? inst = new RPEngine() : inst;
    }

    @Override
    public void enable() {

        ConfigurationSection c = Main.config.get();

        String uri = c.getString("uri");
        if (uri == null || uri.isEmpty()) {
            LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Ресурс-пак не был загружен: неверная ссылка.");
            return;
        }

        String sha1 = c.getString("sha1");
        if (sha1 == null || sha1.isEmpty() || validateHash(sha1)) {
            LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Ресурс-пак не был загружен: неверный SHA1.");
            return;
        }

        resourcePack = new ResourcePack(sha1, uri);

        LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Серверный ресурс-пак загружен ", resourcePack, ".");
    }

    @Override
    public void disable() {
        PlayerJoinEvent.getHandlerList().unregister(this);
        resourcePack = null;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void loadResourcePacksOnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (resourcePack != null) {
            resourcePack.loadForPlayer(p);
        }
    }

    public boolean updateResourcePack(String sha1, String uri) {
        try {
            resourcePack = new ResourcePack(sha1, uri);
            Main.config.get().set("uri", resourcePack.getUri());
            Main.config.get().set("sha1", resourcePack.getHash());
            Main.config.save();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeResourcePack() {
        if (resourcePack != null) {
            resourcePack = null;
            Main.config.get().set("uri", null);
            Main.config.get().set("sha1", null);
            Main.config.save();
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateUri(String uri) {
        return !LINK_REGEX.matcher(uri).matches();
    }

    public static boolean validateHash(String sha1) {
        return sha1.length() != 40;
    }
}

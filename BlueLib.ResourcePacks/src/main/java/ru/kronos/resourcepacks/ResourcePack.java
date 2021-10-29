package ru.kronos.resourcepacks;

import org.bukkit.craftbukkit.libs.org.apache.commons.codec.DecoderException;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Hex;
import org.bukkit.entity.Player;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.extra.LoggingLevel;

public class ResourcePack {

    private String uri;
    private byte[] hash;

    public ResourcePack(String sha1, String uri) {
        setUri(uri);
        setHash(sha1);
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setHash(String hash) {
        try {
            this.hash = Hex.decodeHex(hash.toCharArray());
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    }

    public String getHash() {
        return new String(Hex.encodeHex(this.hash));
    }

    public String getUri() {
        return uri;
    }

    public void loadForPlayer(Player player) {
        LogEngine.debugMsg(LoggingLevel.INFO, Main.inst.getName(), " | Начинаю подгрузку серверного ресурс-пака игроку ", player.getName(), ".");
        player.setResourcePack(uri, hash);
    }

    @Override
    public String toString() {
        return "[ URI=" + getUri() + ", SHA1=" + getHash() + " ]";
    }
}

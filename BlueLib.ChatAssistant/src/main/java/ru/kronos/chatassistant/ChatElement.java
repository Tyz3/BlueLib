package ru.kronos.chatassistant;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.ConfigurationSection;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.util.Utils;
import ru.kronos.bluelib.extra.LoggingLevel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ChatElement {

    private static final Set<ChatElement> DEFAULT_CHAT_ELEMENTS = new HashSet<>();
    private static final Set<ChatElement> CHAT_ELEMENTS = new HashSet<>();

    public static final ChatElement PRFX =          new ChatElement("PRFX", true);
    public static final ChatElement SUFF =          new ChatElement("SUFF", true);
    public static final ChatElement DISP =          new ChatElement("DISP", true);
    public static final ChatElement MESG =          new ChatElement("MESG", true);
    public static final ChatElement CH_PRFX =       new ChatElement("CH_PRFX", true);
    public static final ChatElement DSRV_LNK =      new ChatElement("DSRV_LNK", true);
    public static final ChatElement DSRV_ULNK =     new ChatElement("DSRV_ULNK", true);

    private final String NAME;
    private TextComponent component;

    private ChatElement(String name, boolean def) {
        NAME = name;
        if (def) {
            DEFAULT_CHAT_ELEMENTS.add(this);
            CHAT_ELEMENTS.add(this);
        }
    }

    public String getName() {
        return NAME;
    }

    public TextComponent getComponent() {
        return component.duplicate();
    }

    public ChatElement reloadByConfig(ConfigurationSection c) {
        component = Utils.createTextComponent(c);
        LogEngine.debugMsg(LoggingLevel.DEBUG, Main.inst.getName(),
                " | Инициализирован элемент чата ", NAME);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatElement that = (ChatElement) o;
        return NAME.equals(that.NAME);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NAME);
    }

    @Override
    public String toString() {
        return "ChatElement{" +
                "NAME='" + NAME + '\'' +
                '}';
    }

    public void register() {
        if (!CHAT_ELEMENTS.add(this) && !DEFAULT_CHAT_ELEMENTS.contains(this)) {
            LogEngine.debugMsg(LoggingLevel.WARNING, Main.inst.getName(),
                    " | Инициализация существующего чат-элемента: ", this.NAME);
        }
    }

    public static ChatElement createChatElement(String name) {
        return hasChatElement(name) ? getChatElement(name) : new ChatElement(name, false);
    }

    public static boolean hasChatElement(String name) {
        for (ChatElement ce : CHAT_ELEMENTS) {
            if (ce.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }

    public static ChatElement getChatElement(String name) {
        for (ChatElement ce : CHAT_ELEMENTS) {
            if (ce.getName().equals(name)) {
                return ce;
            }
        }

        return null;
    }

    public static void clear() {
        CHAT_ELEMENTS.clear();
        CHAT_ELEMENTS.addAll(DEFAULT_CHAT_ELEMENTS);
    }

    public static int count() {
        return CHAT_ELEMENTS.size();
    }


}

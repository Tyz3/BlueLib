package ru.kronos.gamephase;

import org.bukkit.Material;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.gamephase.onetime.OneTimeAction;
import ru.kronos.gamephase.regular.RegularRule;
import ru.kronos.gamephase.regular.RegularRules;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamePhase {

    private final String NAME;
    private final List<OneTimeAction> JOIN = new ArrayList<>();
    private final List<OneTimeAction> EXIT = new ArrayList<>();
    private final RegularRules REGULAR = new RegularRules();

    public GamePhase(String name) {
        NAME = name;
    }

    public String getName() {
        return NAME;
    }

    public void addJoinAction(OneTimeAction action) {
        JOIN.add(action);
        LogEngine.debugMsg(LoggingLevel.DEBUG,
                Main.inst.getName(), " | В фазу ", NAME, "-JOIN добавлено действие ", action.toString());
    }

    public void addExitAction(OneTimeAction action) {
        EXIT.add(action);
        LogEngine.debugMsg(LoggingLevel.DEBUG,
                Main.inst.getName(), " | В фазу ", NAME, "-EXIT добавлено действие ", action.toString());
    }

    private void logSetRegularRule(String ruleName) {
        LogEngine.debugMsg(LoggingLevel.DEBUG,
                Main.inst.getName(), " | В фазу ", NAME, "-REGULAR добавлено постоянное правило ", ruleName);
    }

    public void setRegularRule(RegularRule rule) {
        REGULAR.setRegularRule(rule);
        logSetRegularRule(rule.toString());
    }

    public void performJoinActions(BlueLibPlayer player) {
        if (!JOIN.isEmpty()) {
            JOIN.forEach(a -> a.perform(player));
        }
    }

    public void performExitActions(BlueLibPlayer player) {
        if (!EXIT.isEmpty()) {
            EXIT.forEach(a -> a.perform(player));
        }
    }

    public void tick(BlueLibPlayer player) {
        if (REGULAR.hasEffectRule()) {
            player.addPotionEffects(REGULAR.getEffectRule().getEffects());
        }
    }

    public boolean hasGod(EntityDamageEvent.DamageCause cause) {
        return REGULAR.hasGodRule() && REGULAR.getGodRule().hasGod(cause);
    }

    public boolean isItemInteractAllowed(ItemStack item) {
        return !REGULAR.hasItemRule() || REGULAR.getItemRule().isItemAllowed(item);
    }

    public boolean isBlockBreakAllowed(Material block) {
        return !REGULAR.hasBlockRule() || REGULAR.getBlockRule().isBlockBreakAllowed(block);
    }

    public boolean isBlockPlaceAllowed(Material block) {
        return !REGULAR.hasBlockRule() || REGULAR.getBlockRule().isBlockPlaceAllowed(block);
    }

    public boolean isBlockInteractAllowed(Material block) {
        return !REGULAR.hasBlockRule() || REGULAR.getBlockRule().isBlockInteractAllowed(block);
    }

    public boolean isCommandAllowed(String cmd) {
        return !REGULAR.hasCommandRule() || REGULAR.getCommandRule().isCommandAllowed(cmd);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GamePhase gamePhase = (GamePhase) o;
        return NAME.equals(gamePhase.NAME);
    }

    @Override
    public int hashCode() {
        return Objects.hash(NAME);
    }

    @Override
    public String toString() {
        return "GamePhase{" +
                "NAME='" + NAME + '\'' +
                '}';
    }
}

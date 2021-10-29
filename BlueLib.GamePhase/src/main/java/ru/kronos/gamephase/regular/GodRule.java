package ru.kronos.gamephase.regular;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Set;
import java.util.stream.Collectors;

public class GodRule extends RegularRule {

    private final boolean godAtAll;
    private final Set<EntityDamageEvent.DamageCause> causes;

    private GodRule(ConfigurationSection c) {
        godAtAll = c.getBoolean("god-rule.all", false);
        causes = c.getStringList("god-rule.for").stream()
                .map(EntityDamageEvent.DamageCause::valueOf).collect(Collectors.toSet());
    }

    public boolean hasGod(EntityDamageEvent.DamageCause cause) {
        return godAtAll || causes.contains(cause);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public static RegularRule createFromConfig(ConfigurationSection c) {
        return new GodRule(c);
    }
}

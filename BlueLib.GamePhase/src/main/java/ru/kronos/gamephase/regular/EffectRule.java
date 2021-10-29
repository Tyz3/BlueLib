package ru.kronos.gamephase.regular;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import ru.kronos.bluelib.api.util.Utils;

public class EffectRule extends RegularRule {

    public PotionEffect[] effects;

    private EffectRule(ConfigurationSection c) {
        effects = c.getStringList("effects").stream()
                .map(Utils::deserializePotionEffect).toArray(PotionEffect[]::new);
    }

    public PotionEffect[] getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public static RegularRule createFromConfig(ConfigurationSection c) {
        return new EffectRule(c);
    }
}

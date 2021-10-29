package ru.kronos.gamephase.onetime;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.Utils;

public class GiveEffects extends OneTimeAction {

    private final PotionEffect[] EFFECTS;

    private GiveEffects(ConfigurationSection c) {
        if (c.contains("effects") && c.isList("effects")) {
            EFFECTS = Utils.deserializePotionEffects(c.getStringList("effects")).toArray(PotionEffect[]::new);
        } else {
            EFFECTS = null;
        }
    }

    @Override
    public void perform(BlueLibPlayer player) {
        player.addPotionEffects(EFFECTS);
    }

    @Override
    public boolean initialized() {
        return EFFECTS != null;
    }

    public static OneTimeAction createFromConfig(ConfigurationSection c) {
        return new GiveEffects(c);
    }
}

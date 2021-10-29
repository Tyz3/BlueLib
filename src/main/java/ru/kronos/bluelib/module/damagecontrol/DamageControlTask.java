package ru.kronos.bluelib.module.damagecontrol;

import org.bukkit.event.entity.EntityDamageEvent;
import ru.kronos.bluelib.Setting;
import ru.kronos.bluelib.api.engine.DamageControlEngine;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibTask;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.bluelib.api.util.ThreadDaemon;
import ru.kronos.bluelib.extra.LoggingLevel;

import java.util.Map;

public class DamageControlTask extends BlueLibTask {

    private static DamageControlTask inst;

    private DamageControlTask() {}

    public static DamageControlTask getInstance() {
        return inst == null ? inst = new DamageControlTask() : inst;
    }

    @Override
    public void run() {
        for (BlueLibPlayer player : OnlineEngine.getOnline().values()) {
            Map<EntityDamageEvent.DamageCause, Double> deferredDamage = player.getDeferredDamage();

            if (deferredDamage.isEmpty()) continue;

            // Просуммировали весь накопленный урон
            double damage = deferredDamage.values().stream().mapToDouble(v -> v).sum();

            if (deferredDamage.containsKey(EntityDamageEvent.DamageCause.POISON)
                    || deferredDamage.containsKey(EntityDamageEvent.DamageCause.WITHER)
                    || deferredDamage.containsKey(EntityDamageEvent.DamageCause.STARVATION)) {
                player.nonLethalDamage(damage);
            } else {
                player.damage(damage);
            }

            LogEngine.debugMsg(LoggingLevel.DEBUG, DamageControlEngine.class.getSimpleName(),
                    " | Игрок ", player, " получил задержанный урон ", damage, " ед. Причины: ", deferredDamage);

            deferredDamage.clear();
        }
    }

    @Override
    public void enable() {
        task = ThreadDaemon.syncTimer(this, 0L, Setting.DamageControlEngine_dealDamageFrequencyTicks.getLong());
    }
}

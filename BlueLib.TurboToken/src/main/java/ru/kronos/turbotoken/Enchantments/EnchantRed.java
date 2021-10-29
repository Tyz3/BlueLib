package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

@Deprecated
public class EnchantRed extends EnchantTemplate {
	
	public EnchantRed(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double chance;

	public void apply(Event event, int level) {
		EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
		
		if (!(e.getDamager() instanceof Player)) return;
		Player p = (Player) e.getDamager();
		p.getItemInHand();
		Entity en = e.getEntity();
        if (p.getLocation().getDirection().dot(en.getLocation().getDirection()) > 0) {
        	if (MathOperation.procChance(chance)) {
            	en.setFireTicks(10);
                p.sendMessage("[BACKSTAB LISTENER] Backstab");
                e.setDamage(e.getDamage() * (1 + level));
            }
        }
		
	}
	
	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		chance = c.getDouble("chance", 100F);
	}
	
	
}

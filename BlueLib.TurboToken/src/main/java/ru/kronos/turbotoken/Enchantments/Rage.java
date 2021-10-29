package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.turbotoken.Templates.EnchantTemplate;
import ru.kronos.turbotoken.TurboPlayer;

public class Rage extends EnchantTemplate {
	
	public Rage(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double damagePerLevel;
	private double healthTrigger;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		damagePerLevel = c.getDouble("damagePerLevel", 7D);
		healthTrigger = c.getDouble("healthTrigger", 7D);
	}

	public double apply(TurboPlayer killer, double damage, int level) {
		return killer.p.getHealth() <= healthTrigger ? damage + level * damagePerLevel : damage;
	}
}

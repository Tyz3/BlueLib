package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import ru.kronos.turbotoken.Templates.EnchantTemplate;
import ru.kronos.turbotoken.TurboPlayer;

public class Spikes extends EnchantTemplate {
	
	public Spikes(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double reflectionPerLevel;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		reflectionPerLevel = c.getDouble("reflectionPerLevel", .02D);
	}
	
	public void apply(TurboPlayer killer, double damage, int level) {
		killer.hit(damage * reflectionPerLevel * level);
	}
	
	public void apply(LivingEntity killer, double damage, int level) {
		killer.damage(damage * reflectionPerLevel * level);
	}

}

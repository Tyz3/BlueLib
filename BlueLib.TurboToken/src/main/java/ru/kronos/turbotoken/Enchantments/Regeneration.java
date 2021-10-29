package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class Regeneration extends EnchantTemplate {
	
	public Regeneration(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double healthPerLevel;
	
	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		healthPerLevel = c.getDouble("healthPerLevel", .1D);
	}
	
	@Override
	public String getDisplayString(int level) {
		return super.getDisplayString("+", MathOperation.roundAvoid(level*healthPerLevel, roundPlaces));
	}
	
	public double apply(double regen, int level) {
		return regen + level * healthPerLevel;
	}

}

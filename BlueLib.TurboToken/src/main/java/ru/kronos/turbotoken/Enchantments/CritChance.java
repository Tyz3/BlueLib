package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class CritChance extends EnchantTemplate {
	
	public CritChance(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double chancePerLevel;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		chancePerLevel = c.getDouble("chancePerLevel", .1D);
	}
	
	@Override
	public String getDisplayString(int level) {
		double value = MathOperation.roundAvoid(chancePerLevel*level*100, roundPlaces);
		return super.getDisplayString(value > 0 ? "+" : "", value);
	}
	
	public boolean apply(int level) {
		return MathOperation.procChance(chancePerLevel*level);
	}
}

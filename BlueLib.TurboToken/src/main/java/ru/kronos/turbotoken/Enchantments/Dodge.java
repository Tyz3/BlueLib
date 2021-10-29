package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class Dodge extends EnchantTemplate {

	public Dodge(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double chancePerLevel;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		chancePerLevel = c.getDouble("chancePerLevel", .001D);
	}
	
	@Override
	public String getDisplayString(int level) {
		double value = chancePerLevel*level*100;
		return super.getDisplayString(value > 0 ? "+" : "", MathOperation.roundAvoid(value, roundPlaces));
	}
	
	public boolean apply(int level) {
		return level != 0 && MathOperation.procChance(chancePerLevel * level);
	}
}

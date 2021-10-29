package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class CritPower extends EnchantTemplate {

	public CritPower(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double powerPerLevel;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		powerPerLevel = c.getDouble("powerPerLevel", 1D);
	}
	
	@Override
	public String getDisplayString(int level) {
		return super.getDisplayString("+", MathOperation.roundAvoid(level*powerPerLevel, roundPlaces));
	}
	
	public double apply(double damage, int level) {
		return damage + powerPerLevel*level;
	}
}

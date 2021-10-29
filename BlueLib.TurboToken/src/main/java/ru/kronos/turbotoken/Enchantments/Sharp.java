package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class Sharp extends EnchantTemplate {

	public Sharp(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}
	
	private double startChance;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		startChance = c.getDouble("startChance", 1D) * 100D;
	}
	
	@Override
	public String getDisplayString(int level) {
		return super.getDisplayString("", startChance / Math.pow(2, level - 1));
	}

}

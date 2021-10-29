package ru.kronos.turbotoken.TechnicalEnchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class RepairMaterial extends EnchantTemplate {

	public RepairMaterial(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	@Override
	public void reload(ConfigurationSection c) { }

}

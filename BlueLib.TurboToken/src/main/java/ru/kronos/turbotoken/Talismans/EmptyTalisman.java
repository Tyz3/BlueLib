package ru.kronos.turbotoken.Talismans;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.turbotoken.Templates.TalismanTemplate;

public class EmptyTalisman extends TalismanTemplate {
	
	public EmptyTalisman(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}
	
	@Override
	public void reload(ConfigurationSection c) { }
	
}

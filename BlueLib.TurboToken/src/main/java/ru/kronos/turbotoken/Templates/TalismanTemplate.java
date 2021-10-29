package ru.kronos.turbotoken.Templates;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.turbotoken.Setting;

public abstract class TalismanTemplate extends EnchantTemplate {

	public TalismanTemplate(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}
	
	@Override
	public void reload(ConfigurationSection c) {
		display = c.getString("display", super.getKey().getNamespace());
		
		String displayFormat = c.getString("override", Setting.talismanLine.getString());
		
		createViewFormat(displayFormat, c);
	}
	
	@Override
	public String getDisplayString(int number) {
		return super.getDisplayString(number).replace("{number}", String.valueOf(number));
	}
}

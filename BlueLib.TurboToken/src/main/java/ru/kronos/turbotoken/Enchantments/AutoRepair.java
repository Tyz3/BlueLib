package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class AutoRepair extends EnchantTemplate {

	public AutoRepair(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}
	
	private short repairAmountPerLevel;
	
	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		repairAmountPerLevel = (short) c.getInt("repairAmountPerLevel", 500);
	}
	
	public void apply(ItemStack item, int level) {
		short dur = item.getDurability();
		dur -= repairAmountPerLevel;
		item.setDurability(dur < 0 ? 0 : dur);
	}
	

}

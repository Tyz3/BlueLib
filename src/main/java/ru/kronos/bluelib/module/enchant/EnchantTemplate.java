package ru.kronos.bluelib.module.enchant;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import ru.kronos.bluelib.Main;

public abstract class EnchantTemplate extends Enchantment {
	
	private final int MAX_LEVEL;
	private final EnchantmentTarget ENCHANTMENT_TARGET;
	private final boolean CURSED;
	private final boolean TREASURE;
	
	public EnchantTemplate(String name, EnchantmentTarget enchantmentTarget, int maxLevel, boolean CURSED, boolean TREASURE) {
		super(new NamespacedKey(Main.inst, name));
		this.ENCHANTMENT_TARGET = enchantmentTarget;
		this.MAX_LEVEL = maxLevel;
		this.CURSED = CURSED;
		this.TREASURE = TREASURE;
	}
	
	// 1710
//	public EnchantTemplate(int id, EnchantmentTarget enchantmentTarget, String name, int maxLevel) {
//		super(id);
//		this.ENCHANTMENT_TARGET = enchantmentTarget;
//		this.NAME = name;
//		this.MAX_LEVEL = maxLevel;
//	}
//	
//	
//	@Override
//	public int hashCode() {
//		return getId();
//	}
//	
//	@Override
//	public String toString() {
//		return "[ Id: "+getId()+", Name: "+NAME+", Target: "+ENCHANTMENT_TARGET+", MaxLevel: "+MAX_LEVEL+", StartLevel: "+START_LEVEL+" ]";
//	}
	
	public abstract void reload(ConfigurationSection c);
	
	@Override
	public String toString() {
		return "[ NamespacedKey: "+getKey()+", Target: "+ENCHANTMENT_TARGET+", MaxLevel: "+MAX_LEVEL+" ]";
	}
	
	@Override
	public boolean canEnchantItem(ItemStack arg0) {
		return true;
	}
	
	@Override
	public boolean conflictsWith(Enchantment arg0) {
		return false;
	}
	
	@Override
	public EnchantmentTarget getItemTarget() {
		return ENCHANTMENT_TARGET;
	}
	
	@Override
	public int getMaxLevel() {
		return MAX_LEVEL;
	}

	@Override
	public int getStartLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return getKey().getKey();
	}

	@Override
	public boolean isCursed() {
		return CURSED;
	}

	@Override
	public boolean isTreasure() {
		return TREASURE;
	}
}

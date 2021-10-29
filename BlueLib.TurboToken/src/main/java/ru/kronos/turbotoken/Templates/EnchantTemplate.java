package ru.kronos.turbotoken.Templates;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import ru.kronos.bluelib.api.engine.EnchantEngine;
import ru.kronos.bluelib.api.util.Utils;
import ru.kronos.turbotoken.Main;
import ru.kronos.turbotoken.Setting;

public abstract class EnchantTemplate extends ru.kronos.bluelib.module.enchant.EnchantTemplate {
	
	protected String display;       // Отображаемое название чара.
	protected String viewFormat;    // Шаблон строки для предмета с подстановками (line1/line2).
	protected boolean visible;      // Нужно ли отображать на предмете.
	protected int roundPlaces = 1;  // Стандартное округление до 1 позиции мантиссы.

	
	public EnchantTemplate(EnchantmentTarget enTarget, String name, int maxLevel, boolean cursed, boolean treasure) {
		super(name, enTarget, maxLevel, cursed, treasure);
		
		EnchantEngine.registerNewEnchant(this);
		
		if (this instanceof TalismanTemplate) {
			Main.TALISMANS.add((TalismanTemplate) this);
		} else {
			Main.ENCHANTMENTS.add(this);
		}
	}
	
	public EnchantTemplate(EnchantmentTarget enTarget, String name, int maxLevel) {
		this(enTarget, name, maxLevel, false, false);
	}
	
	/**
	 * Устанавливаент базовые настройки, такие как
	 * displayName, useLevel, visible, roundPlaces.
	 * @param c конфиг этого зачарования.
	 */
	public void reload(ConfigurationSection c) {
		display = c.getString("display", super.getKey().getNamespace());
		visible = c.getBoolean("visible", true);
		roundPlaces = c.getInt("roundPlaces", 1);
		
		String displayFormat = Setting.valueOf(c.getString("displayFormat", "line1")).getString();
		displayFormat = c.getString("override", displayFormat);
		
		createViewFormat(displayFormat, c);
	}
	
	protected void createViewFormat(String displayFormat, ConfigurationSection c) {
		viewFormat = displayFormat.replace("{c}", c.getString("color", "&7")).replace("{display}", display).replace("&", "§");
	}
	
	public String getDisplayString(int level) {
		return viewFormat.replace("{value}", Setting.useRomanNumerals.getBool() ? String.valueOf(level) : Utils.translateArabicToRoman(level));
	}
	
	protected String getDisplayString(String sign, double value) {
		return getDisplayString(sign, (int)value);
	}
	
	protected String getDisplayString(String sign, int value) {
		return viewFormat.replace("{sign}", sign).replace("{value}", String.valueOf(value));
	}

	public boolean isVisible() {
		return visible;
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
		return super.getItemTarget();
	}

	@Override
	public int getMaxLevel() {
		return super.getMaxLevel();
	}

	@Override
	public int getStartLevel() {
		return super.getStartLevel();
	}
	

}

package ru.kronos.bluelib.api.engine;

import org.bukkit.enchantments.Enchantment;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.template.BlueLibEngine;
import ru.kronos.bluelib.module.enchant.EnchantTemplate;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public final class EnchantEngine extends BlueLibEngine {
	
	private static final Set<EnchantTemplate> enchantments = new HashSet<>();

	private static EnchantEngine inst;

	private EnchantEngine() {}

	public static EnchantEngine getInstance() {
		return inst == null ? inst = new EnchantEngine() : inst;
	}
	
	@Override
	public void enable() {
		setEnabled(true);
	}
	
	@Override
	public void disable() {
		setEnabled(false);
	}
	
	public static boolean registerNewEnchant(EnchantTemplate enchantment) {
		
		if (enchantments.contains(enchantment)) {
			LogEngine.debugMsg(LoggingLevel.ERROR, EnchantEngine.class.getSimpleName(), " | Ошибка. Регистрация дубликата пользовательского чара: ", enchantment, ".");
			return false;
		}
		
		try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchantment);
            enchantments.add(enchantment);
            f.setAccessible(false);
            return true;
        } catch (Exception e) {
			LogEngine.debugMsg(LoggingLevel.ERROR, EnchantEngine.class.getSimpleName(), " | Ошибка в регистрации пользовательского чара: ", enchantment);
        	e.printStackTrace();
        	return false;
        }
	}
	
	public static Set<EnchantTemplate> getRegisteredEnchants() {
		return enchantments;
	}
	
}

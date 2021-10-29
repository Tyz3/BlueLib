package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class Kickback extends EnchantTemplate {
	
	public Kickback(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private int multiplyPerLevel;
	private int yBoostPerLevel;
	
	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		multiplyPerLevel = c.getInt("multiplyPerLevel", 2);
		yBoostPerLevel = c.getInt("yBoostPerLevel", 1);
	}
	
	public void applyPVP(BlueLibPlayer killer, BlueLibPlayer victim, int level) {
		victim.getBukkitPlayer().setVelocity(killer.getLocation().getDirection().multiply(multiplyPerLevel * level).setY(yBoostPerLevel * level));
	}

}

package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.turbotoken.Templates.EnchantTemplate;

public class SlownessSpeed extends EnchantTemplate {
	
	public SlownessSpeed(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private float bonusSpeedPerLevel;
	
	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		bonusSpeedPerLevel = (float) c.getDouble("bonusSpeedPerLevel", 0.1F);
	}
	
	@Override
	public String getDisplayString(int level) {
		double value = MathOperation.roundAvoid(-bonusSpeedPerLevel*level*500, roundPlaces);
		return super.getDisplayString("", value);
	}
	
	public float apply(float walkSpeed, int level) {
		return walkSpeed - bonusSpeedPerLevel*level;
	}
}

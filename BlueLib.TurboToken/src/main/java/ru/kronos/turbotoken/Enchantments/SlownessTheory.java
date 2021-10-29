package ru.kronos.turbotoken.Enchantments;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.turbotoken.Templates.EnchantTemplate;
import ru.kronos.turbotoken.TurboPlayer;

public class SlownessTheory extends EnchantTemplate {
	
	public SlownessTheory(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double chancePerLevel;
	private int durationPerStep;
	private int levelsByStep;
	private int startDuration;
	private int amplifier;
	private long cooldown;
	
	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		chancePerLevel = c.getDouble("chancePerLevel", .005D);
		durationPerStep = c.getInt("duration", 40);
		levelsByStep = c.getInt("levelsByStep", 10);
		startDuration = c.getInt("startDuration", 20);
		amplifier = c.getInt("amplifier", 1);
		cooldown = c.getLong("cooldown", 0L);
	}
	
	@Override
	public String getDisplayString(int level) {
		double value = MathOperation.roundAvoid(chancePerLevel*level*100, roundPlaces);
		return super.getDisplayString("+", value);
	}
	
	public void apply(TurboPlayer killer, LivingEntity victim, int level) {
		if (killer.hasEnchantCooldown(this)) return;
		killer.setEnchantCooldown(this, cooldown);
		if (!MathOperation.procChance(chancePerLevel * level)) return;
		PotionEffect pe = new PotionEffect(PotionEffectType.SLOW, startDuration + durationPerStep * (level / levelsByStep), amplifier, true);
		pe.apply(victim);
	}
	
}

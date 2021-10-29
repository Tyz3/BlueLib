package ru.kronos.turbotoken.Potions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import ru.kronos.turbotoken.Templates.PotionTemplate;
import ru.kronos.turbotoken.TurboPlayer;

public class BerserkBoostDamage extends PotionTemplate {
	
	private double maxDamageBoost;
	private float speedBoost;
	private int hungerStep;

	public BerserkBoostDamage(String name, boolean instant) {
		super(name, instant);
	}
	
	public BerserkBoostDamage(String name, String displayName, boolean instant, int duration, int amplifier, boolean ambient, int applySpeed,
			double maxDamageBoost, float speedBoost, int hungerStep) {
		super(name, displayName, instant, duration, amplifier, ambient, applySpeed);
		this.maxDamageBoost = maxDamageBoost;
		this.speedBoost = speedBoost;
		this.hungerStep = hungerStep;
	}
	
	@Override
	public PotionTemplate clone() {
		return new BerserkBoostDamage(name, displayName, instant, duration, amplifier, ambient, applySpeed, maxDamageBoost, speedBoost, hungerStep);
	}

	@Override
	public void reload(ConfigurationSection c) {
		super.setBasicValues(c);
		maxDamageBoost = c.getDouble("maxDamageBoost", 20D);
		speedBoost = (float) c.getDouble("speedBoost", .2F);
		hungerStep = c.getInt("hungerStep", 1);
	}
	
	public double invoke(Player p, double damage) {
		double bonus = maxDamageBoost - ((Damageable) p).getHealth();
		return damage + (Math.max(bonus, 0D));
	}
	
	public float invokeBoostSpeed(float walkSpeed) {
		return walkSpeed + speedBoost;
	}
	
	@Override
	public void invoke(TurboPlayer p) {
		int foodLevel = p.p.getBukkitPlayer().getFoodLevel() - hungerStep;
		p.p.getBukkitPlayer().setFoodLevel(Math.max(foodLevel, 0));
	}

}

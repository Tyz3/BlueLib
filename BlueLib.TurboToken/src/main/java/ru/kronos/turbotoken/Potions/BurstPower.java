package ru.kronos.turbotoken.Potions;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.kronos.turbotoken.Templates.PotionTemplate;
import ru.kronos.turbotoken.TurboPlayer;

public class BurstPower extends PotionTemplate {
	
	private double damage;
	
	public BurstPower(String name, boolean instant) {
		super(name, instant);
	}
	
	public BurstPower(String name, String displayName, boolean instant, int duration, int amplifier, boolean ambient, int applySpeed, double damage) {
		super(name, displayName, instant, duration, amplifier, ambient, applySpeed);
		this.damage = damage;
	}

	@Override
	public PotionTemplate clone() {
		return new BurstPower(name, displayName, instant, duration, amplifier, ambient, applySpeed, damage);
	}

	public double invoke(Player p, double damage) {
		// TODO Добавить спавн частиц от действия бафа.
		return damage + this.damage;
	}
	
	@Override
	public void reload(ConfigurationSection c) {
		super.setBasicValues(c);
		damage = c.getDouble("damage", 0D);
	}
	
	@Override
	public void invoke(TurboPlayer p) { }
	
	
}

package ru.kronos.turbotoken.Talismans;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.turbotoken.Templates.TalismanTemplate;
import ru.kronos.turbotoken.TurboPlayer;

@Deprecated
public class FirstBlood extends TalismanTemplate {
	
	public FirstBlood(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double chance;
	private long cooldown;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		chance = c.getDouble("chance", 1D);
		cooldown = c.getLong("cooldown", 0L);
	}
	
	public void apply(TurboPlayer p) {
		if (p.hasEnchantCooldown(this)) return;
		p.setEnchantCooldown(this, cooldown);
		if (!MathOperation.procChance(chance)) {
		}
//		TT.BURST_POWER.clone().apply(p.p);
	}

}

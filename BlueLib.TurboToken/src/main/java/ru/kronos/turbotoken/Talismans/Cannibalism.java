package ru.kronos.turbotoken.Talismans;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.turbotoken.Templates.TalismanTemplate;

public class Cannibalism extends TalismanTemplate {
	
	public Cannibalism(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private int hpPerLevel;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		hpPerLevel = c.getInt("hpPerLevel", 1);
	}
	
	public void apply(BlueLibPlayer killer, int level) {
		switch (level) {
		case 1:
			killer.setHealth(killer.getHealth() + hpPerLevel);
			break;
		case 2:
			killer.setHealth(killer.getHealth() + (hpPerLevel << 1));
			break;
		default:
			killer.setHealth(killer.getHealth() + hpPerLevel * 3);
			break;
		}
	}

}

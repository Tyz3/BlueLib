package ru.kronos.turbotoken.Enchantments;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.turbotoken.Templates.EnchantTemplate;
import ru.kronos.turbotoken.TurboPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class Madness extends EnchantTemplate {
	
	public Madness(EnchantmentTarget enTarget, String name, int maxLevel) {
		super(enTarget, name, maxLevel);
	}

	private double radius;
	private double sharePerLevel;

	@Override
	public void reload(ConfigurationSection c) {
		super.reload(c);
		radius = c.getDouble("radius", 2.5D);
		sharePerLevel = c.getDouble("sharePerLevel", .02D);
	}
	
	public void apply(TurboPlayer killer, LivingEntity victim, double damage, int level) {
		damage = damage * sharePerLevel * level;
		
		for (Entity entity : getNearest(killer.p, victim)) {
			EntityDamageEvent e = new EntityDamageEvent(entity, DamageCause.ENTITY_ATTACK, damage);
			Bukkit.getPluginManager().callEvent(e);
			if (!e.isCancelled()) ((Damageable) entity).damage(damage);
		}
	}
	
	private List<Entity> getNearest(BlueLibPlayer p, LivingEntity exclude) {
		return p.getNearbyEntities(radius, radius, radius).stream()
				.filter(entity -> entity instanceof LivingEntity && !(entity instanceof Player) && exclude != entity)
				.collect(Collectors.toList());
	}
	
}

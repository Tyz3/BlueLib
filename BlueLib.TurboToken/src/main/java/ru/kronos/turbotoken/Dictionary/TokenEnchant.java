package ru.kronos.turbotoken.Dictionary;

import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.turbotoken.Enchantments.*;
import ru.kronos.turbotoken.TechnicalEnchantments.*;

public class TokenEnchant {
	
	// SPECIAL
	public static final Sharp SHARP =                                     new Sharp(EnchantmentTarget.ALL, "SHARP", 100);
	
	// TECHNICAL
	public static final Id ID =                                              new Id(EnchantmentTarget.ALL, "ID", Integer.MAX_VALUE/5);
	public static final Specialization SPECIALIZATION =          new Specialization(EnchantmentTarget.ALL, "SPECIALIZATION", 100);
	public static final Quality QUALITY =                               new Quality(EnchantmentTarget.ALL, "QUALITY", 100);
	public static final RepairMaterial REPAIR_MATERIAL =         new RepairMaterial(EnchantmentTarget.ALL, "REPAIR_MATERIAL", 100);
	public static final Type TYPE =                                        new Type(EnchantmentTarget.ALL, "TYPE", 100);
	
	// ENCHANTS
	public static final Rage RAGE =                                        new Rage(EnchantmentTarget.WEAPON, "RAGE", 25);
	public static final Madness MADNESS =                               new Madness(EnchantmentTarget.WEAPON, "MADNESS", 25);
	public static final Kickback KICKBACK =                            new Kickback(EnchantmentTarget.WEAPON, "KICKBACK", 5);
	public static final Regeneration REGENERATION =                new Regeneration(EnchantmentTarget.ARMOR, "REGENERATION", 50);
	public static final Dodge DODGE =                                     new Dodge(EnchantmentTarget.WEAPON, "DODGE", 50);

	public static final Spikes SPIKES =                                  new Spikes(EnchantmentTarget.ARMOR, "SPIKES", 3);
	public static final MovementSpeed MOVEMENT_SPEED =            new MovementSpeed(EnchantmentTarget.WEAPON, "MOVEMENT_SPEED", 200); // -20%
	public static final SlownessSpeed SLOWNESS_SPEED =            new SlownessSpeed(EnchantmentTarget.ARMOR, "SLOWNESS_SPEED", 180); // -18%
	public static final CritPower CRIT_POWER =                        new CritPower(EnchantmentTarget.ALL, "CRIT_POWER", 1000);
	public static final CritChance CRIT_CHANCE =                     new CritChance(EnchantmentTarget.ALL, "CRIT_CHANCE", 250);
	
	public static final SlownessTheory SLOWNESS_THEORY =         new SlownessTheory(EnchantmentTarget.ALL, "SLOWNESS_THEORY", 20);
	public static final PoisonTheory POSION_THEORY =               new PoisonTheory(EnchantmentTarget.ALL, "POSION_THEORY", 20);
	public static final WitherTheory WITHER_THEORY =               new WitherTheory(EnchantmentTarget.ALL, "WITHER_THEORY", 20);
	public static final BlindnessTheory BLINDNESS_THEORY =      new BlindnessTheory(EnchantmentTarget.ALL, "BLINDNESS_THEORY", 20);

	public static final AutoRepair AUTO_REPAIR =                     new AutoRepair(EnchantmentTarget.ALL, "AUTO_REPAIR", 3);
}

package ru.kronos.turbotoken.Dictionary;

import org.bukkit.enchantments.EnchantmentTarget;
import ru.kronos.turbotoken.Talismans.Cannibalism;
import ru.kronos.turbotoken.Talismans.EmptyTalisman;

public class TokenTalisman {

	public static final EmptyTalisman EMPTY_TALISMAN =            new EmptyTalisman(EnchantmentTarget.ALL, "EMPTY_TALISMAN", 4);
	public static final Cannibalism CANNIBALISM =                   new Cannibalism(EnchantmentTarget.WEAPON, "CANNIBALISM", 3);
//	public static final FirstBlood FIRST_BLOOD =                     new FirstBlood(EnchantmentTarget.WEAPON, "FIRST_BLOOD", 1);
	
}

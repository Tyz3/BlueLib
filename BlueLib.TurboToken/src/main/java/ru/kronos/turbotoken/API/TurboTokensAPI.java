package ru.kronos.turbotoken.API;

import ru.kronos.turbotoken.Main;
import ru.kronos.turbotoken.Templates.EnchantTemplate;
import ru.kronos.turbotoken.Templates.TalismanTemplate;

import java.util.List;

public class TurboTokensAPI {
	
	public static Main getAPI() {
		return Main.inst;
	}
	
	public static List<TalismanTemplate> getTalismans() {
		return Main.TALISMANS;
	}
	
	public static List<EnchantTemplate> getEnchantments() {
		return Main.ENCHANTMENTS;
	}
	
}

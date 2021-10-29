package ru.kronos.turbotoken;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.ConfigEngine;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.BlueLibPlugin;
import ru.kronos.bluelib.api.template.config.BukkitConfig;
import ru.kronos.turbotoken.Dictionary.TokenEnchant;
import ru.kronos.turbotoken.Dictionary.TokenPotion;
import ru.kronos.turbotoken.Dictionary.TokenTalisman;
import ru.kronos.turbotoken.Templates.EnchantTemplate;
import ru.kronos.turbotoken.Templates.PotionTemplate;
import ru.kronos.turbotoken.Templates.TalismanTemplate;

import java.util.*;

public class Main extends BlueLibPlugin {
	
	public static Main inst;

	public static final List<EnchantTemplate> ENCHANTMENTS = new ArrayList<>();
	public static final List<TalismanTemplate> TALISMANS = new ArrayList<>();
	public static final List<PotionTemplate> POTIONS = new ArrayList<>();
	
	public static final Map<UUID, TurboPlayer> ONLINE = new HashMap<>();

	public static BukkitConfig mainConfig;
	public static BukkitConfig enchantsConfig;
	public static BukkitConfig talismansConfig;
	public static BukkitConfig potionsConfig;
	
	@Override
	public void load() {
		inst = this;
		
		mainConfig = ConfigEngine.registerNewConfig(this, "config.yml");
		enchantsConfig = ConfigEngine.registerNewConfig(this, "enchants.yml");
		talismansConfig = ConfigEngine.registerNewConfig(this, "talismans.yml");
		potionsConfig = ConfigEngine.registerNewConfig(this, "potions.yml");
		
		new TokenEnchant();
		new TokenTalisman();
		new TokenPotion();
	}

	@Override
	public void enable() {
		mainConfig.reload();
		enchantsConfig.reload();
		talismansConfig.reload();
		potionsConfig.reload();

		Setting.load(mainConfig.getConfigurationSection("settings"));
		Message.load(mainConfig.getConfigurationSection("messages"));

		TokenEngine.reload();

		// INIT... ENCHANTS
		for (EnchantTemplate et : ENCHANTMENTS) {
			try {
				ConfigurationSection c = enchantsConfig.getConfigurationSection(et.getClass().getSimpleName());
				et.reload(c);
				LogEngine.debugMsg(LoggingLevel.INFO, "[", this.getName(),"] Зачарование ", et.getClass().getSimpleName(), " успешно загружено.");
			} catch (Exception e) {
				LogEngine.debugMsg(LoggingLevel.ERROR, "[", this.getName(),"] Ошибка при загрузке чара ", et, ".");
				e.printStackTrace();
			}
		}

		LogEngine.debugMsg(LoggingLevel.MINIMUM, "[", this.getName(),"] Всего загружено ", ENCHANTMENTS.size(), " пользовательских зачарований.");

		// INIT... AMULETS
		for (EnchantTemplate at : TALISMANS) {
			try {
				ConfigurationSection c = talismansConfig.getConfigurationSection(at.getClass().getSimpleName());
				at.reload(c);
				LogEngine.debugMsg(LoggingLevel.INFO, "[", this.getName(),"] Талисман ", at.getClass().getSimpleName(), " успешно загружен.");
			} catch (Exception e) {
				LogEngine.debugMsg(LoggingLevel.ERROR, "[", this.getName(),"] Ошибка при загрузке талисмана ", at, ".");
				e.printStackTrace();
			}
		}

		LogEngine.debugMsg(LoggingLevel.MINIMUM, "[", this.getName(),"] Всего загружено ", TALISMANS.size(), " талисманов.");

		// INIT... POTIONS
		for (PotionTemplate cp : POTIONS) {
			try {
				ConfigurationSection c = potionsConfig.getConfigurationSection(cp.getClass().getSimpleName());
				cp.reload(c);
				LogEngine.debugMsg(LoggingLevel.INFO, "[", this.getName(),"] Зелье ", cp.getClass().getSimpleName(), " успешно загружено.");
			} catch (Exception e) {
				LogEngine.debugMsg(LoggingLevel.ERROR, "[", this.getName(),"] Ошибка при загрузке зелья ", cp, ".");
				e.printStackTrace();
			}
		}

		LogEngine.debugMsg(LoggingLevel.MINIMUM, "[", this.getName(),"] Всего загружено ", TALISMANS.size(), " пользовательских эффектов зелий.");
	}

	@Override
	public void disable() {

	}
	
	public static EnchantTemplate getEnchant(Enchantment enchantment) {
		for (EnchantTemplate e : ENCHANTMENTS) {
			if (e.equals(enchantment)) return e;
		}

		return null;
	}
	
	public static TalismanTemplate getTalisman(Enchantment talisman) {
		for (TalismanTemplate a : TALISMANS) {
			if (a.equals(talisman)) return a;
		}

		return null;
	}
	
	public static PotionTemplate getPotion(PotionTemplate potion) {
		for (PotionTemplate p : POTIONS) {
			if (p.equals(potion)) return p;
		}

		return null;
	}
	
	public static TurboPlayer getTurboPlayer(Player p) {
		return ONLINE.get(p.getUniqueId());
	}
	
}

package ru.kronos.bluelib.api.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Utils {
	
	
	/**
	 * Если reverseOrder = true - сортировка по убыванию, иначе по возрастанию.
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean reverseOrder) {
		if (reverseOrder) {
			Map<K,V> topTen =
				    map.entrySet().stream()
				       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				       .collect(Collectors.toMap(
				          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			return topTen;
		} else {
			Map<K,V> topTen =
				    map.entrySet().stream()
				       .sorted(Map.Entry.comparingByValue())
				       .collect(Collectors.toMap(
				          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			return topTen;
		}
	}
	
	/**
	 * Если reverseOrder = true - сортировка по убыванию, иначе по возрастанию.
	 * @limit максимальное число элементов после сортировки.
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean reverseOrder, int limit) {
		if (reverseOrder) {
			Map<K,V> topTen =
				    map.entrySet().stream()
				       .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
					   .limit(limit)
				       .collect(Collectors.toMap(
				          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			return topTen;
		} else {
			Map<K,V> topTen =
				    map.entrySet().stream()
				       .sorted(Map.Entry.comparingByValue())
					   .limit(limit)
				       .collect(Collectors.toMap(
				          Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
			return topTen;
		}
	}
	
	/**
	 * @param enchants чары предмета в виде Map.
	 * @return Возвращает чары в формате json вида: {id:0,lvl:10},{...}, ...
	 */
	public static String rawEnchants(Map<Enchantment, Integer> enchants) {
		if (enchants == null || enchants.isEmpty()) return "[]";
		StringBuilder sb = new StringBuilder();
		// {id:0,lvl:10},{...}
		sb.append("[");
		int i = 0;
		for (Entry<Enchantment, Integer> e : enchants.entrySet()) {
			// TODO
//			sb.append("{id:");
//			sb.append(e.getKey().getId());
			sb.append(",lvl:");
			sb.append(e.getValue());
			sb.append("}");
			if (i < enchants.size()-1) sb.append(",");
			i++;
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * @param lore лор предмета в виде списка.
	 * @return Возвращает лор в формате json вида: [\"string1\",\"string2\", ...]
	 */
	public static String rawLore(List<String> lore) {
		if (lore == null || lore.isEmpty()) return "[]";
		StringBuilder sb = new StringBuilder();
		// "lore1","lore2"
		sb.append("[");
		for (int i = 0; i < lore.size(); i++) {
			sb.append("\\\"");
			sb.append(lore.get(i).equals("") ? " " : lore.get(i));
			sb.append("\\\"");
			if (i < lore.size()-1) sb.append(",");
		}
		sb.append("]");
		return sb.toString().replace(":", "");
	}
	
	/**
	 * @param array список строк для предмета.
	 * @return Возвращает лор в формате json вида: ["",{"text":"string1 \n string2 \n ..."}]
	 */
	public static String rawArray(List<String> array) {
		if (array == null || array.isEmpty()) return "[\"\",{\"text\":\"\"}]";
		StringBuilder sb = new StringBuilder();
		// "lore1","lore2"
		sb.append("[\"\",{\"text\":\"");
		for (int i = 0; i < array.size(); i++) {
			sb.append("§b");
			sb.append(array.get(i));
			if (i < array.size()-1) sb.append("\n");
		}
		sb.append("\"}]");
		return sb.toString();
	}

	/**
	 * @param array массив строк для предмета.
	 * @return Возвращает лор в формате json вида: ["",{"text":"string1 \n string2 \n ..."}]
	 */
	public static String rawArray(String[] array) {
		if (array == null || array.length == 0) return "[\"\",{\"text\":\"\"}]";
		StringBuilder sb = new StringBuilder();
		// "lore1","lore2"
		sb.append("[\"\",{\"text\":\"");
		for (int i = 0; i < array.length; i++) {
			sb.append("§b");
			sb.append(array[i]);
			if (i < array.length-1) sb.append("\n");
		}
		sb.append("\"}]");
		return sb.toString();
	}
	
	public static String translateArabicToRoman(int arabic) {
		switch (arabic) {
			case 1: return "I";
			case 2: return "II";
			case 3: return "III";
			case 4: return "IV";
			case 5: return "V";
			case 6: return "VI";
			case 7: return "VII";
			case 8: return "VIII";
			case 9: return "IX";
			case 10: return "X";
			
			default: return "ОЧЕНЬ МНОГО";
		}
	}
	
	public static String byte2Hex(byte b[]) {
        java.lang.String hs = "";
        java.lang.String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = java.lang.Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toLowerCase();
    }
 
    public static byte hex2Byte(char a1, char a2) {
        int k;
        if (a1 >= '0' && a1 <= '9') k = a1 - 48;
        else if (a1 >= 'a' && a1 <= 'f') k = (a1 - 97) + 10;
        else if (a1 >= 'A' && a1 <= 'F') k = (a1 - 65) + 10;
        else k = 0;
        k <<= 4;
        if (a2 >= '0' && a2 <= '9') k += a2 - 48;
        else if (a2 >= 'a' && a2 <= 'f') k += (a2 - 97) + 10;
        else if (a2 >= 'A' && a2 <= 'F') k += (a2 - 65) + 10;
        else k += 0;
        return (byte) (k & 0xff);
    }
 
    public static byte[] hex2Byte(String str) {
        int len = str.length();
        if (len % 2 != 0) return null;
        byte r[] = new byte[len / 2];
        int k = 0;
        for (int i = 0; i < str.length() - 1; i += 2) {
            r[k] = hex2Byte(str.charAt(i), str.charAt(i + 1));
            k++;
        }
        return r;
    }

    public static List<PotionEffect> deserializePotionEffects(List<String> raws) {
		List<PotionEffect> effects = new ArrayList<>();

		for (String raw : raws) {
			PotionEffect effect = deserializePotionEffect(raw);

			if (effect != null) {
				effects.add(effect);
			}
		}

		return effects;
	}

	/**
	 * @param raw строка вида: "POTION_TYPE:duration:aplifier:isAmbient:hasParticles:hasIcon"
	 */
    public static PotionEffect deserializePotionEffect(String raw) {
		String[] args = raw.split(":");

		if (args.length < 3) {
			return null;
		}

		String name = args[0].toUpperCase();
		int duration = Integer.parseInt(args[1]);
		int amplifier = Integer.parseInt(args[2]);

		PotionEffectType type = PotionEffectType.getByName(name);
		if (type == null) {
			return null;
		}

		boolean icon = true;
		boolean particles = true;
		boolean ambient = true;

		switch (args.length) {
			case 6:
				icon = Boolean.parseBoolean(args[5]);
			case 5:
				particles = Boolean.parseBoolean(args[4]);
			case 4:
				ambient = Boolean.parseBoolean(args[5]);
		}

		return new PotionEffect(type, duration, amplifier, ambient, particles, icon);
	}

	public static TextComponent createTextComponent(ConfigurationSection c) {
		TextComponent comp = new TextComponent();

		String text = c.getString("text");
		if (text == null) return comp;

		comp.setText(text.replace("&", "§"));

		String color = c.getString("color");
		if (color != null) {
			comp.setColor(ChatColor.of(color.toUpperCase()));
		}

		String style = c.getString("style");
		if (style != null) {
			setStyle(comp, style);
		}

		List<String> styles = c.getStringList("style");
		if (!styles.isEmpty()) {
			styles.forEach(s -> setStyle(comp, s));
		}

		String tip = c.getString("tip");
		if (tip != null) {
			HoverEvent he = new HoverEvent(
					HoverEvent.Action.SHOW_TEXT,
					new TextComponent[]{
							new TextComponent(tip.replace("&", "§"))
					}
			);

			comp.setHoverEvent(he);
		}

		List<String> tips = c.getStringList("tip");
		if (!tips.isEmpty()) {
			BaseComponent[] comps = new TextComponent[tips.size()];

			for (int i = 0; i < comps.length; i++) {
				comps[i] = new TextComponent(tips.get(i).replace("&", "§"));
			}

			HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT, comps);

			comp.setHoverEvent(he);
		}

		String сlickEvent = c.getString("сlickEvent");
		String clickContent = c.getString("clickContent");

		if (!(сlickEvent == null || clickContent == null)) {
			ClickEvent.Action action = ClickEvent.Action.valueOf(сlickEvent.toUpperCase());
			comp.setClickEvent(new ClickEvent(action, clickContent));
		}

		return comp;
	}

	public static TextComponent collectComponents(BaseComponent[] bcomps) {
		TextComponent result = new TextComponent();
		for (BaseComponent bcomp : bcomps) {
			result.addExtra(bcomp);
		}

		return result;
	}

	private static void setStyle(TextComponent comp, String style) {
		switch (style.toLowerCase()) {
			case "bold":
				comp.setBold(true);
				break;
			case "italic":
				comp.setItalic(true);
				break;
			case "underline":
				comp.setUnderlined(true);
				break;
			case "strikethrough":
				comp.setStrikethrough(true);
				break;
			case "obfuscated":
				comp.setObfuscated(true);
				break;
		}
	}
}

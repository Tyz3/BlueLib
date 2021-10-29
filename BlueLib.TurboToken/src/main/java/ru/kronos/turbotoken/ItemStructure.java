package ru.kronos.turbotoken;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kronos.turbotoken.Dictionary.TokenEnchant;
import ru.kronos.turbotoken.Dictionary.TokenTalisman;
import ru.kronos.turbotoken.Templates.EnchantTemplate;
import ru.kronos.turbotoken.Templates.TalismanTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class ItemStructure {
	
	private final ItemStack item;
	private final ItemMeta meta;
	private final List<String> enchantsArea = new ArrayList<>();
	private final List<String> propertiesArea = new ArrayList<>();
	
	public ItemStructure(ItemStack item) {
		this.item = item;
		this.meta = item.getItemMeta();
	}
	
	public void build() {
		
		if (meta.hasLore()) {
			meta.setLore(new ArrayList<>());
			item.setItemMeta(meta);
		}
		
		// Enchants and Specials
		for (EnchantTemplate myEnchant : Main.ENCHANTMENTS) {
			for (Entry<Enchantment, Integer> itemEnchant : meta.getEnchants().entrySet()) {
				if (!myEnchant.equals(itemEnchant.getKey())) continue;
				
				
				// ОБЛАСТЬ ОСОБЫХ СВОЙСТВ
				
				// Позиция отображения чара Рунического усиления.
				if (myEnchant.equals(TokenEnchant.SHARP)) {
					propertiesArea.add(0, myEnchant.getDisplayString(itemEnchant.getValue()));
					continue;
				}
				
				// ОБЛАСТЬ ЧАР
				
				if (myEnchant.isVisible()) enchantsArea.add(myEnchant.getDisplayString(itemEnchant.getValue()));
			}
		}
		
		
		// ТАЛИСМАНЫ
		int talismansAmount = 0;
		TalismanTemplate emptyTalisman = null;
		int emptySlots = 0;
		for (TalismanTemplate myTalisman : Main.TALISMANS) {
			for (Entry<Enchantment, Integer> itemTalisman : meta.getEnchants().entrySet()) {
				if (!myTalisman.equals(itemTalisman.getKey())) continue;
				
				// Позиция слота под талисманы (технический чар).
				if (TokenTalisman.EMPTY_TALISMAN.equals(myTalisman)) {
					emptyTalisman = myTalisman;
					emptySlots = itemTalisman.getValue();
					continue;
				}
				
				talismansAmount++;
				
				// Добавление в область Особых свойств название талисмана.
				propertiesArea.add(myTalisman.getDisplayString(talismansAmount));
				
				// Установка отображаемого чара талисмана.
//				if (myTalisman.hasDisplayName()) enchantsArea.add(myTalisman.getDisplayString(itemTalisman.getValue()));
				
			}
		}
		
		
		// Если Пустой слот есть на предмете, то добавляется запись об оставшихся слотах.
		if (emptyTalisman != null && talismansAmount < emptySlots) {
			propertiesArea.add(Setting.talismanEmptyLine.getString()
					.replace("{number}", String.valueOf(talismansAmount))
					.replace("{all}", String.valueOf(emptySlots)));
		}
		
		buildItemLore();
	}
	
	private void buildItemLore() {
		List<String> newLore = new ArrayList<String>(enchantsArea.size() + propertiesArea.size() + 1);
		newLore.addAll(enchantsArea);
		if (!propertiesArea.isEmpty()) newLore.add("");
		newLore.addAll(propertiesArea);
		
		meta.setLore(newLore);
		item.setItemMeta(meta);
	}
	
	public static void refresh(ItemStack item) {
		new ItemStructure(item).build();
	}
}

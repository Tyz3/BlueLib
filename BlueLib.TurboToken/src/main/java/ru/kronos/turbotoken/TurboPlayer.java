package ru.kronos.turbotoken;

import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;
import ru.kronos.turbotoken.Dictionary.TokenEnchant;
import ru.kronos.turbotoken.Dictionary.TokenPotion;
import ru.kronos.turbotoken.Templates.EnchantTemplate;
import ru.kronos.turbotoken.Templates.PotionTemplate;
import ru.kronos.turbotoken.Templates.TalismanTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TurboPlayer {
	
	public BlueLibPlayer p;
	
	private final Map<EnchantTemplate, Long> cooldowns = new HashMap<>();
	
	public Map<EnchantTemplate, Integer> enchants = new HashMap<>();
	public Map<TalismanTemplate, Integer> talismans = new HashMap<>();
	public List<PotionTemplate> potions = new ArrayList<>();
	
	public TurboPlayer(BlueLibPlayer p) {
		this.p = p;
	}
	
	@Override
	public String toString() {
		return "[ Player: "+p+", Enchants: "+enchants+", Talismans: "+talismans+", Potions: "+potions+" ]";
	}
	
	public void setEnchantCooldown(EnchantTemplate e, long cooldown) {
		cooldowns.put(e, System.currentTimeMillis() + cooldown*1000);
	}
	
	public boolean hasEnchantCooldown(EnchantTemplate e) {
		if (cooldowns.containsKey(e)) {
			if (cooldowns.get(e) < System.currentTimeMillis()) {
				cooldowns.remove(e);
				return false;
			} else return true;
		}
		return false;
	}
	
	public void synchronize() {
		try {
			enchants.clear();
			talismans.clear();
			
			ItemStack[] armor = p.getInventory().getArmorContents();
			
			for (int i = -1; i < armor.length; i++) {
				if (i == -1) {
					ItemStack hand = p.getItemInHand();
					if (hand != null) parse(hand);
					continue;
				}
				
				if (armor[i] == null) continue;
				
				parse(armor[i]);
			}
		} catch (Exception e) {
			LogEngine.debugMsg(LoggingLevel.CRITICAL, TurboPlayer.class.getSimpleName(), " | Ошибка в синхронизации пользовательских чаров игрока (", p, ").");
			e.printStackTrace();
		}
	}
	
	private void parse(ItemStack item) {
		Map<Enchantment, Integer> map = item.getEnchantments();
		if (map.isEmpty()) return;
		for (Entry<Enchantment, Integer> e : map.entrySet()) {
			
			if (e.getKey() instanceof TalismanTemplate) {
				int power = 0;
				if (talismans.containsKey(e.getKey())) {
					power += talismans.get(e.getKey());
				}
				talismans.put((TalismanTemplate) e.getKey(), power + e.getValue());
			} else if (e.getKey() instanceof EnchantTemplate) {
				int power = 0;
				if (enchants.containsKey(e.getKey())) {
					power += enchants.get(e.getKey());
				}
				enchants.put((EnchantTemplate) e.getKey(), power + e.getValue());
			}
		}
	}
	
	public void tickPotions() {
		for (int i = 0;; i++) {
			if (i >= potions.size()) break;
			PotionTemplate potion = potions.get(i);
			if (potion.isEnded()) {
				potions.remove(potion);
				i--;
				continue;
			}
			if (!potion.isInstant() && potion.tick()) potion.invoke(this);
		}
	}
	
	public void changeSpeedMovement() {
		float walkSpeed = 0.2F;
		
		if (enchants.containsKey(TokenEnchant.MOVEMENT_SPEED))
			walkSpeed = TokenEnchant.MOVEMENT_SPEED.apply(walkSpeed, enchants.getOrDefault(TokenEnchant.MOVEMENT_SPEED, 0));
		
		if (enchants.containsKey(TokenEnchant.SLOWNESS_SPEED))
			walkSpeed = TokenEnchant.SLOWNESS_SPEED.apply(walkSpeed, enchants.get(TokenEnchant.SLOWNESS_SPEED));
		
		if (potions.contains(TokenPotion.BERSERK_BOOST_DAMAGE))
			walkSpeed = TokenPotion.BERSERK_BOOST_DAMAGE.invokeBoostSpeed(walkSpeed);
		
		p.setWalkSpeed(walkSpeed > 1F ? 1F : Math.max(walkSpeed, 0F));
	}
	
	public void hit(double damage) {
		p.getBukkitPlayer().damage(damage);
	}
	
	// Здесь активируются амулеты и некоторые чары.
	
	public void invokeMeleeTraits(LivingEntity victim) {
		
	}
	
	public void invokeRangeTraits(Entity projectile, LivingEntity victim) {
		
		
	}
	
	public void invokeArmorTraits(LivingEntity victim) {
		
	}
	
	public void invokeDeathTraits() {
		
		
		
	}
	
	public void invokeFallTraits() {
		
		
		
	}
	
	
	
	
	public Location getLocation() {
		return p.getLocation();
	}
	
	
}

package ru.kronos.bluelib.module.potion;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public abstract class PotionTemplate extends PotionEffectType {
	
	private int performEveryTick;
	
	private int duration;
	private int amplifier;
	private boolean ambient;
	private boolean particles;
	private boolean icon;
	
	protected PotionTemplate(int id, int performEveryTick, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
		super(id);
		this.setPerformEveryTick(performEveryTick);
		this.setDuration(duration);
		this.setAmplifier(amplifier);
		this.setAmbient(ambient);
		this.setParticles(particles);
		this.setIcon(icon);
	}
	
	public PotionEffect getPotionEffect() {
		return new PotionEffect(this, duration, amplifier, ambient, particles, icon);
	}
	
	/**
	 * Добавляет PotionEffect к предмету зелья.
	 */
	public void addToItem(ItemStack potionItem) {
		PotionMeta meta = (PotionMeta) potionItem.getItemMeta();
		meta.addCustomEffect(getPotionEffect(), true);
		potionItem.setItemMeta(meta);
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getAmplifier() {
		return amplifier;
	}

	public void setAmplifier(int amplifier) {
		this.amplifier = amplifier;
	}

	public boolean isAmbient() {
		return ambient;
	}

	public void setAmbient(boolean ambient) {
		this.ambient = ambient;
	}

	public boolean isParticles() {
		return particles;
	}

	public void setParticles(boolean particles) {
		this.particles = particles;
	}

	public boolean isIcon() {
		return icon;
	}

	public void setIcon(boolean icon) {
		this.icon = icon;
	}

	public int getPerformEveryTick() {
		return performEveryTick;
	}

	/**
	 * Каждые performEveryTick тиков выполняется действие зелья.
	 */
	public void setPerformEveryTick(int performEveryTick) {
		this.performEveryTick = performEveryTick;
	}
	
	
	
}

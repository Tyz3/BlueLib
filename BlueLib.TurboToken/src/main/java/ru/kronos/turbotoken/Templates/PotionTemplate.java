package ru.kronos.turbotoken.Templates;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.kronos.turbotoken.Main;
import ru.kronos.turbotoken.Message;
import ru.kronos.turbotoken.TurboPlayer;

import java.util.List;

public abstract class PotionTemplate implements Cloneable {
	
	protected final String name;
	protected String displayName;
	protected int duration;
	protected int amplifier;
	protected boolean instant;
	protected boolean ambient;
	protected int applySpeed;
	protected List<String> description;
	
	private int ticks;
	private long expires;
	
	public PotionTemplate(String name, boolean instant) {
		this.name = name;
		this.instant = instant;
		Main.POTIONS.add(this);
	}
	
	public PotionTemplate(String name, String displayName, boolean instant, int duration, int amplifier, boolean ambient, int applySpeed) {
		this.name = name;
		this.displayName = displayName;
		this.instant = instant;
		this.duration = duration;
		this.expires = System.currentTimeMillis() + duration * 1000L;
		this.amplifier = amplifier;
		this.ambient = ambient;
		this.applySpeed = (this.ticks = applySpeed <= 0 ? 1 : applySpeed);
	}
	
	public abstract PotionTemplate clone();
	public abstract void reload(ConfigurationSection c);
	public abstract void invoke(TurboPlayer p);
	
	@Override
	public String toString() {
		return "[ Name: "+name+", Dur: "+duration+", Ampl: "+amplifier+" ]";
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof PotionTemplate && ((PotionTemplate) o).getName().equalsIgnoreCase(name);
	}
	
	public boolean equals(PotionTemplate o) {
		return o.getName().equalsIgnoreCase(name);
	}
	
	public void apply(Player p) {
		TurboPlayer tp = Main.getTurboPlayer(p);
		if (ambient && tp.potions.contains(this)) return;
		tp.potions.add(this);
		sendStartNotification(p);
	}
	
	public void apply(TurboPlayer p) {
		if (ambient && p.potions.contains(this)) return;
		p.potions.add(this);
	}
	
	public boolean tick() {
		if (ticks > 1) {
			ticks--;
			return false;
		} else {
			ticks = applySpeed;
			return true;
		}
	}
	
	public void sendStartNotification(Player p) {
		Message.potions_started.replace("{name}", getDisplayName()).replace("{duration}", duration).send(p);
	}
	
	public PotionTemplate sendEndNotification(Player p) {
		Message.potions_ended.replace("{name}", getDisplayName()).send(p);
		return this;
	}
	
	public boolean isEnded() {
		return expires < System.currentTimeMillis();
	}
	
	public PotionTemplate setAmplifier(int amplifier) {
		this.amplifier = amplifier;
		return this;
	}
	
	public PotionTemplate setDuration(int duration) {
		this.duration = duration;
		return this;
	}
	
	public PotionTemplate setAmbient(boolean ambient) {
		this.ambient = ambient;
		return this;
	}
	
	public boolean isInstant() {
		return instant;
	}
	
	public boolean isAmbient() {
		return ambient;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName == null ? name : displayName;
	}
	
	public void setBasicValues(ConfigurationSection c) {
		displayName = c.getString("display", getName());
		duration = c.getInt("duration", 10);
		amplifier = c.getInt("amplifier", 0);
		applySpeed = c.getInt("applySpeed", 1);
		ambient = c.getBoolean("ambient", false);
		description = c.getStringList("description");
	}
}

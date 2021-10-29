package ru.kronos.bluelib.api.template.online;

import github.scarsz.discordsrv.dependencies.jda.api.entities.User;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueItemStack;
import ru.kronos.bluelib.extra.LoggingLevel;
import ru.kronos.bluelib.module.chestmenu.Container;
import ru.kronos.bluelib.module.potion.PotionTemplate;
import ru.kronos.bluelib.module.scoreboard.BlueScoreboard;
import ru.kronos.bluelib.api.util.MathOperation;
import ru.kronos.gamephase.GamePhase;

import java.util.*;
import java.util.stream.Collectors;

public class BlueLibPlayer extends BlueLibSender {
	
	private final Player p;
	
	private boolean loaded;
	
	public BlueLibPlayer(Player player) {
		super(player);
		this.p = player;
	}
	
	@Override
	public String toString() {
		return "[ Name: ".concat(getName()).concat(", UUID: ").concat(getUUID().toString()).concat(" ]");
	}
	
	@Override
	public boolean equals(Object o) {
		return o == this;
	}
	
	public boolean equals(String name) {
		return getName().equalsIgnoreCase(name);
	}
	
	// LOADED
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public void setLoaded(boolean value) {
		loaded = value;
	}

	// BlueLib.GamePhase

	public GamePhase gamePhase;

	public GamePhase getGamePhase() {
		return gamePhase;
	}

	public void joinGamePhase(GamePhase newGamePhase) {
		gamePhase.performExitActions(this);
		gamePhase = newGamePhase;
		newGamePhase.performJoinActions(this);
	}

	public void setGamePhase(GamePhase gamePhase) {
		this.gamePhase = gamePhase;
	}

	public void tickGamePhase() {
		if (gamePhase != null) {
			gamePhase.tick(this);
		}
	}

	// VAULT
	
	private double balance;
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void increaseBalance(double value) {
		this.balance += value;
	}
	
	public void decreaseBalance(double value) {
		this.balance -= value;
	}
	
	// DAMAGE CONTROL
	
	private final Map<DamageCause, Double> deferredDamage = new EnumMap<>(DamageCause.class);
	
	public Map<DamageCause, Double> getDeferredDamage() {
		return deferredDamage;
	}
	
	public void setDeferredDamage(DamageCause cause, double damage) {
		deferredDamage.put(cause, damage);
	}
	
	public void damage(double value) {
		p.damage(value);
	}
	
	public void nonLethalDamage(double value) {
		damage(p.getHealth() < value ? p.getHealth() - 1D : value);
	}
	
	// CUSTOM POTIONS
	
	private final Set<PotionTemplate> potions = new HashSet<>();
	
	public Set<PotionTemplate> getActivePotions() {
		return potions;
	}
	
	public void addPotion(PotionTemplate potion) {
		potions.add(potion);
	}
	
	// DISCORDSRV
	
	private String discordId;
	
	public void setDiscordId(String discordId) {
		this.discordId = discordId;
	}
	
	public User getDiscordUser() {
		return DiscordUtil.getJda().getUserById(discordId);
	}
	
	public String getDiscordId() {
		return discordId;
	}
	
	public boolean hasDiscordId() {
		return discordId != null;
	}
	
	public String getDiscordTag() {
		try {
			return getDiscordUser().getAsTag();
		} catch (Exception e) {
			return "";
		}
	}
	
	// PERMISSIONS
	
	private String prefix;
	private String suffix;
	private ChatColor messageColor;
	private ChatColor displayNameColor;
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	public void setMessageColor(String color) {
		if (color != null && !color.equals(""))
			messageColor = ChatColor.of(color);
	}
	
	public void setDisplayNameColor(String color) {
		if (color != null && !color.equals(""))
			displayNameColor = ChatColor.of(color);
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public ChatColor getMessageColor() {
		return messageColor;
	}
	
	public ChatColor getDisplayNameColor() {
		return displayNameColor;
	}
	
	public String getHexMessageColor() {
		return messageColor == null ? "" : messageColor.toString();
	}
	
	public String getHexDisplayNameColor() {
		return displayNameColor == null ? "" : displayNameColor.toString();
	}
	
	public boolean hasPrefix() {
		return !(prefix == null || prefix.equals(""));
	}
	
	public boolean hasSuffix() {
		return !(suffix == null || suffix.equals(""));
	}
	
	public boolean hasMessageColor() {
		return messageColor != null;
	}
	
	public boolean hasDisplayNameColor() {
		return displayNameColor != null;
	}
	
	// SCOREBOARDS
	
	private BlueScoreboard scoreboard;
	
	public void setScoreboard(BlueScoreboard scoreboard) {
		p.setScoreboard(scoreboard.getBukkitScoreboard());
		this.scoreboard = scoreboard;
	}
	
	public void clearScoreboard() {
		if (hasScoreboard()) {
			scoreboard.unregAll();
			scoreboard = null;
			p.setScoreboard(Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard());
		}
	}
	
	public BlueScoreboard getScoreboard() {
		return scoreboard;
	}
	
	public boolean hasScoreboard() {
		return scoreboard != null;
	}
	
	public void clearObjective(DisplaySlot displaySlot) {
		scoreboard.unregObjective(displaySlot);
	}
	
	public void clearTeam(String teamName) {
		scoreboard.unregTeam(teamName);
	}
	
	// CHEST MENU

	private Container currentContainer;
	
	public Container getCurrentContainer() {
		return currentContainer;
	}
	
	public boolean hasCurrentContainer() {
		return currentContainer != null;
	}
	
	public void setCurrentContainer(Container cont) {
		currentContainer = cont;
	}
	
	public void closeInventory() {
		p.closeInventory();
	}
	
	public void openInventory(Inventory inv) {
		p.openInventory(inv);
	}
	
	public InventoryView getOpenInventory() {
		return p.getOpenInventory();
	}
	
	// Common
	
	public UUID getUUID() {
		return p.getUniqueId();
	}
	
	public void sendMessageToNearest(String msg, double radius) {
		OnlineEngine.getOnline().forEach((uuid, o) -> {
			if (MathOperation.distance3D(p.getLocation(), o.getLocation()) <= radius)
				o.sendMessage(msg);
		});
	}
	
	public long getPlayTime() {
		//1710
//		return p.getStatistic(Statistic.PLAY_ONE_TICK) * 50L;
		return p.getStatistic(Statistic.PLAY_ONE_MINUTE) * 60000L;
	}
	
	// POTION EFFECTS
	
	public void addPotionEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles, boolean icon) {
		p.addPotionEffect(new PotionEffect(type, duration, amplifier, ambient, particles, icon));
	}
	
	public void addPotionEffects(Collection<PotionEffect> effects) {
		for (PotionEffect e : effects) {
			addPotionEffect(e.getType(), e.getDuration(), e.getAmplifier(), e.isAmbient(), e.hasParticles(), e.hasIcon());
		}
	}
	
	public void addPotionEffects(PotionEffect... effects) {
		for (PotionEffect e : effects) {
			addPotionEffect(e.getType(), e.getDuration(), e.getAmplifier(), e.isAmbient(), e.hasParticles(), e.hasIcon());
		}
	}

	// ITEMSTACKS

	/**
	 * @param item предмет на проверку.
	 * @return Возвращает номер слота найденного предмета в инвентаре, иначе вернёт -1.
	*/
	public int hasItem(BlueItemStack item) {
		return hasItem(item, 1, false, false, false);
	}
	
	/**
	 * @param item предмет на проверку.
	 * @param amount количество проверяемого предмета.
	 * @return Возвращает номер слота найденного предмета в инвентаре, иначе вернёт -1.
	*/
	public int hasItem(BlueItemStack item, int amount) {
		return hasItem(item, amount, false, false, false);
	}
	
	/**
	 * @param item предмет на проверку.
	 * @param displayName сравнивать ли отображаемые названия предметов.
	 * @param lore сравнивать ли лоры предметов.
	 * @param enchants сравнивать ли зачарования предметов.
	 * @return Возвращает номер слота найденного предмета в инвентаре, иначе вернёт -1.
	*/
	public int hasItem(BlueItemStack item, boolean displayName, boolean lore, boolean enchants) {
		return hasItem(item, 1, displayName, lore, enchants);
	}
	
	/**
	 * @param item предмет на проверку.
	 * @param minAmount количество проверяемого предмета.
	 * @param checkDisplayName сравнивать ли отображаемые названия предметов.
	 * @param checkLore сравнивать ли лоры предметов.
	 * @param checkEnchants сравнивать ли зачарования предметов.
	 * @return Возвращает номер слота найденного предмета в инвентаре, иначе вернёт -1.
	*/
	public int hasItem(BlueItemStack item, int minAmount, boolean checkDisplayName, boolean checkLore, boolean checkEnchants) {
		ItemStack[] contents = p.getInventory().getContents();

		if (item == null) {
			LogEngine.debugMsg(LoggingLevel.WARNING, this.getClass().getSimpleName(), " | Проверяемый предмет null.");
			return -1;
		}

		for (int i = 0; i < contents.length; i++) {
			if (
					item.isSimilar(contents[i], false, checkDisplayName, checkLore, checkEnchants)
					&& contents[i].getAmount() >= minAmount
			) return i;
		}
		
		return -1;
	}
	
	/**
	 * @param items предметы на изъятие.
	*/
	public void withdrawItems(List<ItemStack> items) {
		PlayerInventory inv = p.getInventory();
		items.forEach(i -> inv.remove(i.getType()));
	}
	
	/**
	 * @param item предмет на изъятие.
	 * @return Возвращает номер состояние действия: true если выполнение успешно, иначе - false.
	*/
	public boolean takeItem(ItemStack item) {
		return takeItem(item, 1, false, false, false);
	}
	
	/**
	 * @param item предмет на изъятие.
	 * @param amount количество забираемого предмета.
	 * @return Возвращает номер состояние действия: true если выполнение успешно, иначе - false.
	*/
	public boolean takeItem(ItemStack item, int amount) {
		return takeItem(item, amount, false, false, false);
	}
	
	/**
	 * @param item предмет на изъятие.
	 * @param checkDisplayName сравнивать ли отображаемые названия предметов.
	 * @param checkLore сравнивать ли лоры предметов.
	 * @param checkEnchants сравнивать ли зачарования предметов.
	 * @return Возвращает номер состояние действия: true если выполнение успешно, иначе - false.
	*/
	public boolean takeItem(ItemStack item, boolean checkDisplayName, boolean checkLore, boolean checkEnchants) {
		return takeItem(item, 1, checkDisplayName, checkLore, checkEnchants);
	}
	
	/**
	 * @param item предмет на изъятие.
	 * @param amount количество забираемого предмета.
	 * @param checkDisplayName сравнивать ли отображаемые названия предметов.
	 * @param checkLore сравнивать ли лоры предметов.
	 * @param checkEnchants сравнивать ли зачарования предметов.
	 * @return Возвращает номер состояние действия: true если выполнение успешно, иначе - false.
	*/
	public boolean takeItem(ItemStack item, int amount, boolean checkDisplayName, boolean checkLore, boolean checkEnchants) {
		int slot = hasItem((BlueItemStack) item, amount, checkDisplayName, checkLore, checkEnchants);

		if (slot == -1) {
			return false;
		}

		ItemStack[] contents = p.getInventory().getContents();
		ItemStack i = contents[slot];

		if (i.getAmount() > amount) {
			i.setAmount(i.getAmount() - amount);
		} else {
			i.setType(Material.AIR);
//			p.getInventory().setItem(slot, BlueItemStack.newEmpty());
		}

		p.updateInventory();
		return true;
	}
	
	public Location getLocation() {
		return p.getLocation();
	}
	
	public boolean hasSpace(int cells) {
		ItemStack[] contents = p.getInventory().getContents();
		int airSlots = 0;

		for (ItemStack i : contents) {
			if (i == null || i.getType() == Material.AIR) airSlots++;
		}

		return airSlots >= cells;
	}
	
	public void giveItem(ItemStack item, int amount) {
		ItemStack gi = item.clone();
		gi.setAmount(1);
		for (int i = 0; i < amount; i++) { giveItem(gi); }
	}
	
	public void giveItem(ItemStack item) {
		if (item == null) return;

		if (item.getAmount() > 1) {
			giveItem(item, item.getAmount());
			return;
		}

		if (hasSpace(1)) {
			p.getInventory().addItem(item);
			p.updateInventory();
		} else {
			getWorld().dropItemNaturally(getLocation(), item);
		}
	}
	
	public void giveItems(List<ItemStack> items) {
		items.forEach(this::giveItem);
	}
	
	public void teleport(Location loc) {
		loc.getChunk().load();
		p.teleport(loc);
	}
	
	public World getWorld() {
		return p.getWorld();
	}
	
	public Player getBukkitPlayer() {
		return p;
	}
	
	/**
	 * @param sound - воспроизводимый звук.
	 * @param volume - Громкость звука 0.0 - 1.0.
	 * @param pitch - Скорость воспроизведения: 1.0 - нормально, 2.0 - быстро.
	 */
	public void playSound(Sound sound, float volume, float pitch) {
		p.playSound(getLocation(), sound, volume, pitch);
	}
	
	public void giveExperience(int value) {
		PlayerExpChangeEvent event = new PlayerExpChangeEvent(p, value);
		Bukkit.getPluginManager().callEvent(event);
		p.giveExp(event.getAmount());
	}
	
	public boolean takeExperience(int value) {
		if (p.getTotalExperience() < value) {
			return false;
		}

		recursionTakeExperience(value);
		return true;
	}
	
	private void recursionTakeExperience(int value) {
		int expAtLevel = Math.round(p.getExpToLevel() * p.getExp());

		if (expAtLevel <= value) {
			p.giveExp(-expAtLevel);
			p.setLevel(p.getLevel() - 1);
			p.setExp(1.0F);
			takeExperience(value - expAtLevel);
		} else p.giveExp(-value);
	}
	
	public int getLevel() {
		return p.getLevel();
	}
	
	public boolean takeLevel(int value) {
		if (!hasLevel(value)) {
			return false;
		}

		p.setLevel(getLevel()-value);
		return true;
	}
	
	public boolean hasLevel(int lvl) {
		return getLevel() >= lvl;
	}
	
	public int getTotalExperience() {
		return p.getTotalExperience();
	}
	
	public boolean hasExperience(int exp) {
		return p.getTotalExperience() >= exp;
	}
	
	public double getHealth() {
		return p.getHealth();
	}
	
	public void setHealth(double value) {
		p.setHealth(value);
	}
	
	public void setMaxHealth(double value) {
		p.setMaxHealth(value);
	}
	
	public void setHealthScale(double value) {
		p.setHealthScale(value);
	}
	
	public void setHealthScaled(boolean value) {
		p.setHealthScaled(value);
	}
	
	public boolean isFlying() {
		return p.isFlying();
	}
	
	public void setFlying(boolean value) {
		p.setFlying(value);
	}
	
	public void setAllowFlight(boolean value) {
		p.setAllowFlight(false);
	}
	
	public String getDisplayName() {
		return p.getDisplayName();
	}
	
	// PLAYER STATISTIC
	
	private long firstJoinDate;
	
	public long getFirstJoinDate() {
		return firstJoinDate;
	}
	
	public void setFirstJoinDate(long firstJoinDate) {
		this.firstJoinDate = firstJoinDate;
	}
	
	// PLAYER INVENTORY
	
	public PlayerInventory getInventory() {
		return p.getInventory();
	}
	
	public Inventory getEnderChest() {
		return p.getEnderChest();
	}
	
	public ItemStack getItemInHand() {
		return p.getItemInHand();
	}
	
	// PLAYER INTERACTION
	
	public void setWalkSpeed(float value) {
		p.setWalkSpeed(value);
	}
	
	public List<Entity> getNearbyEntities(double xRadius, double yRadius, double zRadius) {
		return p.getNearbyEntities(xRadius, yRadius, zRadius);
	}

	public Set<Player> getNearbyPlayers(double xRadius, double yRadius, double zRadius) {
		return p.getNearbyEntities(xRadius, yRadius, zRadius).stream().filter(e -> e.getType() == EntityType.PLAYER)
				.map(e -> (Player) e).collect(Collectors.toSet());
	}
}

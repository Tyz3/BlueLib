package ru.kronos.bluelib.api.template;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.kronos.bluelib.Main;
import ru.kronos.bluelib.api.engine.LogEngine;
import ru.kronos.bluelib.extra.LoggingLevel;

import java.util.ArrayList;
import java.util.List;

public class BlueItemStack extends ItemStack implements Cloneable {

    public BlueItemStack() {}

    public BlueItemStack(Material type) {
        super(type);
    }

    public BlueItemStack(Material type, int amount) {
        super(type, amount);
    }

    public BlueItemStack(ItemStack stack) {
        super(stack);
    }

    public static BlueItemStack newEmpty() {
        return new BlueItemStack(Material.AIR, 0);
    }

    public static List<BlueItemStack> parseSimple(List<String> rawItems) {
        List<BlueItemStack> items = new ArrayList<>();

        for (String raw : rawItems) {
            BlueItemStack item = parseSimple(raw);

            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }

    /**
     * @param raw строка формата: "TYPE:durability:amount:displayName"
     */
    public static BlueItemStack parseSimple(String raw) {
        String[] args = raw.split(":");

        BlueItemStack item = null;

        if (args.length > 0) {
            Material type = Material.matchMaterial(args[0]);
            if (type == null) return newEmpty();

            item = new BlueItemStack(type);
        }

        if (args.length > 1) {
            short durability = Short.parseShort(args[1]);
            item.setDurability(durability);
        }

        if (args.length > 2) {
            int amount = Integer.parseInt(args[2]);
            item.setAmount(amount);
        }

        if (args.length > 3) {
            String displayName = args[3].replace("&", "§");
            item.setDisplayName(displayName);
        }

        if (item == null) {
            LogEngine.debugMsg(LoggingLevel.WARNING, " Ошибка инициализации BlueItemStack из '", raw, "'.");
        }

        return item;
    }

    public boolean isSimilar(ItemStack item, boolean checkAmount, boolean checkDisplayName, boolean checkLore, boolean checkEnchants) {

        if (!isSimilar(item)) return false;

        if (checkAmount && getAmount() != item.getAmount()) return false;

        if (item.hasItemMeta() != hasItemMeta()) return false;

        if (hasItemMeta()) {
            ItemMeta m1 = item.getItemMeta();
            ItemMeta m2 = getItemMeta();

            if (checkDisplayName) {
                assert m1 != null;
                assert m2 != null;
                if (m1.hasDisplayName() != m2.hasDisplayName()) return false;

                if (m1.hasDisplayName() && !m1.getDisplayName().equals(m2.getDisplayName())) return false;
            }

            if (checkLore) {
                assert m1 != null;
                assert m2 != null;
                if (m1.hasLore() != m2.hasLore()) return false;

                if (m1.hasLore() && !m1.getLore().containsAll(m2.getLore())) return false;
            }

            if (checkEnchants) {
                assert m1 != null;
                assert m2 != null;
                if (m1.hasEnchants() != m2.hasEnchants()) return false;

                return !m1.hasEnchants() || m1.getEnchants().equals(m2.getEnchants());
            }
        }

        return true;
    }

    public BlueItemStack makeItemAs(ItemStack stack) {
        setType(stack.getType());
        setAmount(stack.getAmount());
        setItemMeta(stack.getItemMeta());
        setData(stack.getData());

        return this;
    }

    // ITEM META

    public boolean hasDisplayName() {
        return hasItemMeta() && getItemMeta().hasDisplayName();
    }

    public String getDisplayName() {
        return hasDisplayName() ? getItemMeta().getDisplayName() : null;
    }

    public void setDisplayName(String displayName) {
        ItemMeta meta = getItemMeta();

        assert meta != null;
        meta.setDisplayName(displayName);
        setItemMeta(meta);
    }

    public boolean hasLore() {
        return hasItemMeta() && getItemMeta().hasLore();
    }

    public List<String> getLore() {
        return hasLore() ? getItemMeta().getLore() : null;
    }

    public void setLore(List<String> lore) {
        ItemMeta meta = getItemMeta();

        assert meta != null;
        meta.setLore(lore);
        setItemMeta(meta);
    }

    @Override
    public BlueItemStack clone() {
        return (BlueItemStack) super.clone();
    }
}

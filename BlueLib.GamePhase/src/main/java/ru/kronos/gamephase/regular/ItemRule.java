package ru.kronos.gamephase.regular;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import ru.kronos.bluelib.api.template.BlueItemStack;

import java.util.Set;
import java.util.stream.Collectors;

public class ItemRule extends RegularRule {

    private final boolean asBlacklist;
    private final Set<BlueItemStack> items;

    private ItemRule(ConfigurationSection c) {
        asBlacklist = c.getBoolean("item-rule.as-blacklist", false);
        items = c.getStringList("item-rule.list").stream()
                .map(BlueItemStack::parseSimple).collect(Collectors.toSet());
    }

    public boolean isItemAllowed(ItemStack item) {
        for (BlueItemStack blueItem : items) {
            if (blueItem.isSimilar(item, false, true, false, false)) {
                return !asBlacklist;
            }
        }

        return asBlacklist;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public static RegularRule createFromConfig(ConfigurationSection c) {
        return new ItemRule(c);
    }
}

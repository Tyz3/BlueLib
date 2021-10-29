package ru.kronos.gamephase.regular;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class BlockRule extends RegularRule {

    enum BlockAction {
        PLACE, BREAK, INTERACT;

        private final Set<Material> blocks = new HashSet<>();

        public static void clear() {
            Arrays.asList(values()).forEach(action -> action.blocks.clear());
        }

        public void addBlocks(Set<Material> types) {
            blocks.addAll(types);
        }

        public boolean contains(Material type) {
            return blocks.contains(type);
        }
    }

    private final boolean asBlacklist;

    private BlockRule(ConfigurationSection c) {
        asBlacklist = c.getBoolean("block-rule.as-blacklist", false);
        BlockAction.clear();

        if (c.contains("block-rule.groups")) {
            ConfigurationSection groups = c.getConfigurationSection("block-rule");

            List<Map<?, ?>> list = groups.getMapList("groups").stream()
                .filter(map -> map.containsKey("actions") && map.containsKey("blocks")).collect(Collectors.toList());

            for (Map<?, ?> map : list) {
                if (!map.containsKey("actions") || !map.containsKey("blocks")) continue;

                Set<BlockAction> actions = ((List<String>) map.get("actions")).stream().map(BlockAction::valueOf)
                        .collect(Collectors.toSet());
                Set<Material> blocks = ((List<String>) map.get("blocks")).stream().map(Material::matchMaterial)
                        .collect(Collectors.toSet());

                actions.forEach(action -> action.addBlocks(blocks));
            }
        }
    }

    public boolean isBlockPlaceAllowed(Material block) {
        return BlockAction.PLACE.contains(block) != asBlacklist;
    }

    public boolean isBlockBreakAllowed(Material block) {
        return BlockAction.BREAK.contains(block) != asBlacklist;
    }

    public boolean isBlockInteractAllowed(Material block) {
        return BlockAction.INTERACT.contains(block) != asBlacklist;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public static RegularRule createFromConfig(ConfigurationSection c) {
        return new BlockRule(c);
    }
}

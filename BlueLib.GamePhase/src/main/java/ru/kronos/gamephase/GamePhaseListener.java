package ru.kronos.gamephase;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.kronos.bluelib.api.engine.OnlineEngine;
import ru.kronos.bluelib.api.template.BlueLibListener;
import ru.kronos.bluelib.api.template.online.BlueLibPlayer;

public class GamePhaseListener extends BlueLibListener {

    private static GamePhaseListener inst;

    private GamePhaseListener() {}

    public static GamePhaseListener getInstance() {
        return inst == null ? inst = new GamePhaseListener() : inst;
    }

    @Override
    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, Main.inst);
    }

    @Override
    public void disable() {
        EntityDamageEvent.getHandlerList().unregister(this);
        BlockBreakEvent.getHandlerList().unregister(this);
        BlockPlaceEvent.getHandlerList().unregister(this);
        PlayerInteractEvent.getHandlerList().unregister(this);
        PlayerCommandPreprocessEvent.getHandlerList().unregister(this);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onPlayerDamage(EntityDamageEvent e) {
        if (EntityType.PLAYER == e.getEntityType()) {
            BlueLibPlayer player = OnlineEngine.getPlayer(e.getEntity().getUniqueId());

            if (player.getGamePhase().hasGod(e.getCause())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onBlockBreak(BlockBreakEvent e) {

//        if (e.getPlayer().hasPermission("gamephase.blocks-bypass")) {
//            return;
//        }

        BlueLibPlayer player = OnlineEngine.getPlayer(e.getPlayer());
        Block block = e.getBlock();

        if (!player.getGamePhase().isBlockBreakAllowed(block.getType())) {
            e.setCancelled(true);
            player.sendMessages(
                    "§cВаша игровая фаза '"
                            .concat(player.getGamePhase().getName())
                            .concat("' запрещает ломать этот блок (§4")
                            .concat(block.getType().name())
                            .concat("§c).")
            );
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onBlockPlace(BlockPlaceEvent e) {
        BlueLibPlayer player = OnlineEngine.getPlayer(e.getPlayer());
        Block block = e.getBlock();

        if (!player.getGamePhase().isBlockPlaceAllowed(block.getType())) {
            e.setCancelled(true);

            player.sendMessage(
                    "§cВаша игровая фаза '"
                            .concat(player.getGamePhase().getName())
                            .concat("' запрещает ставить этот блок (§4")
                            .concat(block.getType().name())
                            .concat("§c).")
            );
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onPlayerBlockInteract(PlayerInteractEvent e) {
        BlueLibPlayer player = OnlineEngine.getPlayer(e.getPlayer());
        Block block = e.getClickedBlock();

        if (e.hasBlock() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.isBlockInHand() && player.getBukkitPlayer().isSneaking()) return;

            if (!player.getGamePhase().isBlockInteractAllowed(block.getType())) {
                e.setCancelled(true);
                player.sendMessages(
                        "§cВаша игровая фаза '"
                                .concat(player.getGamePhase().getName())
                                .concat("' запрещает взаимодействовать с этим блоком (§4")
                                .concat(block.getType().name())
                                .concat("§c).")
                );
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onPlayerItemInteract(PlayerInteractEvent e) {
        BlueLibPlayer player = OnlineEngine.getPlayer(e.getPlayer());
        ItemStack item = e.getItem();

        if (e.isBlockInHand()) return;

        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            if (!player.getGamePhase().isItemInteractAllowed(item)) {
                e.setCancelled(true);
                player.sendMessage(
                        "§cВаша игровая фаза '"
                                .concat(player.getGamePhase().getName())
                                .concat("' запрещает взаимодействовать с этим предметом (§4")
                                .concat(item.getType().name()).concat(":")
                                .concat(String.valueOf(item.getDurability()))
                                .concat("§c).")
                );
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    private void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e) {

//        if (e.getPlayer().hasPermission("gamephase.cmds-bypass")) {
//            return;
//        }

        BlueLibPlayer player = OnlineEngine.getPlayer(e.getPlayer());

        if (!player.getGamePhase().isCommandAllowed(e.getMessage())) {
            e.setCancelled(true);
            player.sendMessages("§cВаша игровая фаза запрещает использование команды §4".concat(e.getMessage()).concat("§c."));
        }
    }
}

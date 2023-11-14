package me.sander.dehydrationplugin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WaterDrinkingListener implements Listener {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 750; // Cooldown time in milliseconds (0.5 seconds)

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        // Check if the player right-clicks with an empty hand
        if (event.getItem() == null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Check if the clicked block is waterlogged
            if (clickedBlock != null && isWaterlogged(clickedBlock) || event.getClickedBlock().getType() == Material.WATER_CAULDRON) {
                // Check if the player is on cooldown
                if (!isOnCooldown(player)) {
                    // Print "drank" to the console
                    System.out.println(player.getName() + " drank");

                    // Add a splash particle effect on top of the targeted block
                    player.getWorld().spawnParticle(Particle.WATER_SPLASH, clickedBlock.getLocation().add(0.5, 1.0, 0.5), 50);

                    // Play the drinking sound
                    player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);

                    // Add 250 to the data container of the player
                    HeatManager.addToPlayerData(player, 2500);

                    // Apply a 0.5-second cooldown
                    applyCooldown(player);
                } else {
                    // Player is on cooldown, you can add a message or other logic here
                }
            }
        }
    }

    private boolean isWaterlogged(Block block) {
        // Check if the block is waterlogged
        return block.getBlockData() instanceof Waterlogged && ((Waterlogged) block.getBlockData()).isWaterlogged();
    }

    private void applyCooldown(Player player) {
        // Set the current time as the cooldown expiration time
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis() + COOLDOWN_TIME);
    }

    private boolean isOnCooldown(Player player) {
        // Check if the player is on cooldown
        return cooldowns.containsKey(player.getUniqueId()) && cooldowns.get(player.getUniqueId()) > System.currentTimeMillis();
    }
}

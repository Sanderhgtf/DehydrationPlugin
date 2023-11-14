package me.sander.dehydrationplugin;

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
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WaterDrinkingListener implements Listener {

    private static final Map<UUID, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_TIME = 550; // Cooldown time in milliseconds (0.6 seconds)

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        // Check if the player right-clicks with an empty hand
        if (event.getItem() == null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Check if the player has the HYDRATION_LEVEL_KEY
            if (player.getPersistentDataContainer().has(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER)) {
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

                        // Add 2500 to the data container of the player
                        add2500ToPlayerData(player);

                        // Apply a 0.5-second cooldown
                        applyCooldown(player);
                    } else {
                        // Player is on cooldown, you can add a message or other logic here
                    }
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

    private void add2500ToPlayerData(Player player) {
        // Retrieve the current hydration level from the player's data container
        int currentHydrationLevel = HeatManager.getHydrationLevel(player);

        // Add 2500 to the current hydration level
        int newHydrationLevel = currentHydrationLevel + 625;

        // Check if the new hydration level exceeds the maximum value of 10000
        if (newHydrationLevel > 3500) {
            // Set the hydration level to the maximum value
            newHydrationLevel = 3500;

            // Send the player a message
            player.sendMessage("Your thirst is quenched!");
        }

        // Set the updated hydration level in the player's data container
        player.getPersistentDataContainer().set(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER, newHydrationLevel);
    }

}

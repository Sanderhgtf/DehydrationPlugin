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

public class WaterDrinkingListener implements Listener {

    private final Map<Player, Long> cooldowns = new HashMap<>();
    private static final long COOLDOWN_DURATION = 750; // Cooldown duration in milliseconds (0.5 seconds)

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        // Check if the player right-clicks with an empty hand and is not on cooldown
        if (event.getItem() == null && event.getAction() == Action.RIGHT_CLICK_BLOCK && !isOnCooldown(player)) {
            // Check if the clicked block is waterlogged or a water-filled cauldron
            if (clickedBlock != null && (isWaterlogged(clickedBlock) || event.getClickedBlock().getType() == Material.WATER_CAULDRON)) {
                // Print "drank" to the console
                System.out.println(player.getName() + " drank");

                // Play the drinking sound at the block location
                player.playSound(clickedBlock.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);

                // Spawn splash particle effect on top of the block
                spawnSplashParticle(clickedBlock.getLocation());

                // Set the player on cooldown
                setCooldown(player);
            }
        }
    }

    private boolean isWaterlogged(Block block) {
        // Check if the block is waterlogged
        return block.getBlockData() instanceof Waterlogged && ((Waterlogged) block.getBlockData()).isWaterlogged();
    }

    private boolean isOnCooldown(Player player) {
        // Check if the player is on cooldown
        return cooldowns.containsKey(player) && System.currentTimeMillis() - cooldowns.get(player) < COOLDOWN_DURATION;
    }

    private void setCooldown(Player player) {
        // Set the player on cooldown
        cooldowns.put(player, System.currentTimeMillis());
    }

    private void spawnSplashParticle(org.bukkit.Location location) {
        // Spawn splash particle effect on top of the location
        location.getWorld().spawnParticle(Particle.WATER_SPLASH, location.add(0.5, 1, 0.5), 10, 0.2, 0.5, 0.2);
    }
}

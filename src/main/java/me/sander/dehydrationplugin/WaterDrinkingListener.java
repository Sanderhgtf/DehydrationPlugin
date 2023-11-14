package me.sander.dehydrationplugin;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

///
public class WaterDrinkingListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();

        // Check if the player right-clicks with an empty hand
        if (event.getItem() == null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Check if the clicked block is waterlogged or a water-filled cauldron
            if (clickedBlock != null && (isWaterlogged(clickedBlock) || event.getClickedBlock().getType() == Material.WATER_CAULDRON)) {
                // Print "drank" to the console
                System.out.println(player.getName() + " drank");

                // Play the drinking sound at the block location
                player.playSound(clickedBlock.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
            }
        }
    }

    private boolean isWaterlogged(Block block) {
        // Check if the block is waterlogged
        return block.getBlockData() instanceof Waterlogged && ((Waterlogged) block.getBlockData()).isWaterlogged();
    }
}

package me.sander.dehydrationplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DehydrationPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        // Determines the loop time.
        Bukkit.getScheduler().runTaskTimer(this, this::checkSunExposure, 0, 20);
    }

    private void checkSunExposure() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            World world = player.getWorld();
            if (world.getTime() >= 0 && world.getTime() < 12300) {
                int blocksInShade = getBlocksInShade(player.getLocation());
                Bukkit.getServer().getConsoleSender().sendMessage(player.getName() + " has " + blocksInShade + " blocks of shade.");
            }
        });
    }

    private int getBlocksInShade(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY() + 1;

        // Initial blocks of shade. This resembles 100% shade. If you change this you also need to change the raycast grid size.
        int blocksInShade = 25;
        // Determines how high up the raycasts go till they stop checking. Lower equals better ram usage.
        int checkHeight = 30;

        for (int offsetX = -2; offsetX <= 2; offsetX++)
            for (int offsetZ = -2; offsetZ <= 2; offsetZ++)
                for (int offsetY = 0; offsetY < checkHeight; offsetY++) {
                    Block block = world.getBlockAt(x + offsetX, y + offsetY, location.getBlockZ() + offsetZ);
                    if (!block.getType().isTransparent()) break;
                    if (offsetY == (checkHeight-1)) blocksInShade--;
                }

        return blocksInShade;
    }
}

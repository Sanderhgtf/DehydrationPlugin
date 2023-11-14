package me.sander.dehydrationplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;


public class HeatManager {

    public static void checkSunExposure() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            World world = player.getWorld();
            if (world.getTime() >= 0 && world.getTime() < 12300) {
                int blocksInShade = getBlocksInShade(player.getLocation());
                Bukkit.getServer().getConsoleSender().sendMessage(player.getName() + " has " + blocksInShade + " blocks of shade.");

                // Check if the player is in a desert biome during the daytime
                if (isInDesertBiome(player.getLocation())) {
                    Bukkit.getServer().getConsoleSender().sendMessage("");
                    Bukkit.getServer().getConsoleSender().sendMessage("Biome: Desert");
                    Bukkit.getServer().getConsoleSender().sendMessage("Time: Day");
                    Bukkit.getServer().getConsoleSender().sendMessage("Shade level = " + blocksInShade);

                    // Print the persistent data value
                    int dataValue = getPlayerDataValue(player);
                    Bukkit.getServer().getConsoleSender().sendMessage("Player's data value: " + dataValue);
                } else {
                    Bukkit.getServer().getConsoleSender().sendMessage("Not in a desert biome during the daytime.");
                }
            }
        });
    }


    private static int getBlocksInShade(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY() + 1;

        int blocksInShade = 25;
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

    private static boolean isInDesertBiome(Location location) {
        World world = location.getWorld();

        // Get temperature and humidity of the biome at the given location
        double temperature = world.getTemperature(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        double humidity = world.getHumidity(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        // Check if temperature and humidity resemble a desert biome
        return temperature > 1.0 && humidity < 0.3;
    }

    private static int getPlayerDataValue(Player player) {
        // Retrieve the persistent data value from the player
        return player.getPersistentDataContainer().get(DatapackKey.DATA_KEY, PersistentDataType.INTEGER);
    }
}

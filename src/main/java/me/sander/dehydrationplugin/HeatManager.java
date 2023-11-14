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

            // Check if it's daytime (between 0 and 12300 ticks)
            if (world.getTime() >= 0 && world.getTime() < 12300) {
                // Check if the player has the HYDRATION_LEVEL_KEY persistent data container
                if (hasHydrationLevelContainer(player)) {
                    int blocksInSun = getBlocksInSun(player.getLocation());

                    // Calculate finalDrain
                    int finalDrain = blocksInSun;

                    // Subtract finalDrain from the player's data container
                    subtractFromPlayerData(player, finalDrain);

                    // Check if the player is in a desert biome during the daytime
                    if (isInDesertBiome(player.getLocation())) {
                        Bukkit.getServer().getConsoleSender().sendMessage("");
                        Bukkit.getServer().getConsoleSender().sendMessage("Biome: Desert");
                        Bukkit.getServer().getConsoleSender().sendMessage("Time: Day");
                        Bukkit.getServer().getConsoleSender().sendMessage("Sun level = " + blocksInSun);

                        // Print the persistent data value after subtraction
                        int dataValue = getPlayerDataValue(player);
                        Bukkit.getServer().getConsoleSender().sendMessage("Player's data value after subtraction: " + dataValue);
                    } else {
                        Bukkit.getServer().getConsoleSender().sendMessage("Not in a desert biome during the daytime.");
                    }
                }
            }
        });
    }

    private static boolean hasHydrationLevelContainer(Player player) {
        // Check if the player has the HYDRATION_LEVEL_KEY persistent data container
        return player.getPersistentDataContainer().has(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER);
    }

    private static int getBlocksInSun(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY() + 1;

        int blocksInSun = 0;
        int checkHeight = 30;

        for (int offsetX = -2; offsetX <= 2; offsetX++)
            for (int offsetZ = -2; offsetZ <= 2; offsetZ++)
                for (int offsetY = 0; offsetY < checkHeight; offsetY++) {
                    Block block = world.getBlockAt(x + offsetX, y + offsetY, location.getBlockZ() + offsetZ);
                    if (!block.getType().isTransparent()) break;
                    if (offsetY == (checkHeight-1)) blocksInSun++;
                }

        return blocksInSun;
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
        return player.getPersistentDataContainer().get(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER);
    }

    private static void subtractFromPlayerData(Player player, int value) {
        // Retrieve the current data value from the player's data container
        int currentDataValue = getPlayerDataValue(player);

        // Subtract the specified value from the current data value
        int newDataValue = currentDataValue - value;

        // Set the updated data value in the player's data container
        player.getPersistentDataContainer().set(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER, newDataValue);
    }

    public static int getHydrationLevel(Player player) {
        return player.getPersistentDataContainer().get(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER);
    }

}

package me.sander.dehydrationplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HeatManager {

    private static void applyDehydrationEffects(Player player) {
        int hydrationLevel = getPlayerDataValue(player);

        if (hydrationLevel < 1000 && hydrationLevel > 500) {

            // Remove previous Slowness and confusion
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.removePotionEffect(PotionEffectType.WEAKNESS);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 0, false, false));

            // Check if the player has enough health to survive, if not, set health to 0
            if (player.getHealth() <= 1.0) {
                EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, 0);
                Bukkit.getServer().getPluginManager().callEvent(damageEvent);

                // Cancel the default damage event
                if (!damageEvent.isCancelled()) {
                    player.setHealth(0);
                }

                // Custom death message
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " died from dehydration!");
            }

        } else if (hydrationLevel < 500 && hydrationLevel > 100) {

            // Remove previous Slowness and confusion
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.removePotionEffect(PotionEffectType.WEAKNESS);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 1, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000000, 0, false, false));

            // Remove 0.5 hearts
            player.damage(1.0);

            // Check if the player has enough health to survive, if not, set health to 0
            if (player.getHealth() <= 1.0) {
                EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, 0);
                Bukkit.getServer().getPluginManager().callEvent(damageEvent);

                // Cancel the default damage event
                if (!damageEvent.isCancelled()) {
                    player.setHealth(0);
                }

                // Custom death message
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " died from dehydration!");
            }

        } else if (hydrationLevel < 100) {

            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.removePotionEffect(PotionEffectType.WEAKNESS);

            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 3, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 1000000, 0, false, false));
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1000000, 4, false, false));

            // Remove 1.0 hearts
            player.damage(2.0);

            // Check if the player has enough health to survive, if not, set health to 0
            if (player.getHealth() <= 1.0) {
                EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.CUSTOM, 0);
                Bukkit.getServer().getPluginManager().callEvent(damageEvent);

                // Cancel the default damage event
                if (!damageEvent.isCancelled()) {
                    player.setHealth(0);
                }

                // Custom death message
                Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " died from dehydration!");
            }

        } else {

            // Remove dehydration effects
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
        }
    }





    public static void checkPlayerStatus(Player player) {
        int hydrationLevel = getPlayerDataValue(player);

        if (player.isDead()) {
            // Player has died, clear effects and set hydration level back to 3500
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.getPersistentDataContainer().set(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER, 3500);

            // Write to the console about the player's death and hydration level reset
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + player.getName() + "'s Hydration level reset to 3500.");
        } else if (hydrationLevel <= 0) {
            // Player is alive but has reached 0 hydration, apply dehydration effects
            applyDehydrationEffects(player);
        }
    }


    public static void checkSunExposure() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            World world = player.getWorld();

            // Check if it's daytime (between 0 and 12300 ticks)
            if (world.getTime() >= 0 && world.getTime() < 12300) {
                // Check if the player has the HYDRATION_LEVEL_KEY persistent data container
                if (hasHydrationLevelContainer(player)) {

                    // Check if the player is in a desert biome during the daytime
                    if (isInDesertBiome(player.getLocation())) {

                        int blocksInSun = getBlocksInSun(player.getLocation());

                        // Calculate finalDrain
                        int finalDrain = blocksInSun;

                        // Subtract finalDrain from the player's data container
                        subtractFromPlayerData(player, finalDrain);
                        applyDehydrationEffects(player);

                        Bukkit.getServer().getConsoleSender().sendMessage("");
                        Bukkit.getServer().getConsoleSender().sendMessage("Biome: Desert");
                        Bukkit.getServer().getConsoleSender().sendMessage("Time: Day");
                        Bukkit.getServer().getConsoleSender().sendMessage("Sun level = " + blocksInSun);

                        // Print the persistent data value after subtraction
                        int dataValue = getPlayerDataValue(player);
                        Bukkit.getServer().getConsoleSender().sendMessage("Player's data value after subtraction: " + dataValue);
                    } else {
                        // Player gains 10 hydration if blocksInSun is 0
                        int blocksInSun = getBlocksInSun(player.getLocation());
                        if (blocksInSun == 0) {
                            int hydrationLevel = getPlayerDataValue(player);
                            int newHydrationLevel = Math.min(hydrationLevel + 10, 10000);
                            player.getPersistentDataContainer().set(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER, newHydrationLevel);
                        }

                        Bukkit.getServer().getConsoleSender().sendMessage("Not in a desert biome during the daytime.");
                    }
                    // death handling
                    checkPlayerStatus(player);
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

        // Ensure that the new data value is not below 0
        newDataValue = Math.max(newDataValue, 0);

        // Set the updated data value in the player's data container
        player.getPersistentDataContainer().set(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER, newDataValue);
    }


    public static int getHydrationLevel(Player player) {
        return player.getPersistentDataContainer().get(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER);
    }

}

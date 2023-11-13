package me.sander.dehydrationplugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/////

public class DehydrationPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the task to run every second (20 ticks)
        Bukkit.getScheduler().runTaskTimer(this, this::checkSunExposure, 0, 20);
    }

    private void checkSunExposure() {
        // Iterate through all online players
        Bukkit.getOnlinePlayers().forEach(player -> {
            World world = player.getWorld();

            // Check if it's daytime in the player's world
            if (world.getTime() >= 0 && world.getTime() < 12300) { // You may adjust these values based on your needs
                // Send message to the console
                Bukkit.getServer().getConsoleSender().sendMessage("Player " + player.getName() + " is exposed to the sun!");
            }
        });
    }
}

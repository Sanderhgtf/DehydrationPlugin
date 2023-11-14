package me.sander.dehydrationplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DehydrationPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Determines the loop time.
        //Bukkit.getScheduler().runTaskTimer(this, HeatManager::checkSunExposure, 0, 100);

        getCommand("givedatacontainer").setExecutor(new GiveDataContainerCommand());

        Bukkit.getPluginManager().registerEvents(new WaterDrinkingListener(), this);
    }
}

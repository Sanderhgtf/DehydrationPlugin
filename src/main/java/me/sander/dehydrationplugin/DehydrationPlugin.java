package me.sander.dehydrationplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class DehydrationPlugin extends JavaPlugin {
    ///

    @Override
    public void onEnable() {
        // Register the WaterDrinkingListener
        getServer().getPluginManager().registerEvents(new WaterDrinkingListener(), this);

        // Register the RemoveDataContainerCommand
        getCommand("removedatacontainer").setExecutor(new RemoveDataContainerCommand());

        // Register the GiveDataContainerCommand
        getCommand("givedatacontainer").setExecutor(new GiveDataContainerCommand());

        // Schedule the heat manager task
        getServer().getScheduler().runTaskTimer(this, HeatManager::checkSunExposure, 0, 200);
    }
}

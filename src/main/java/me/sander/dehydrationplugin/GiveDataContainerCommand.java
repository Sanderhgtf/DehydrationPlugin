package me.sander.dehydrationplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class GiveDataContainerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the player already has a hydration level container
            if (hasDataContainer(player)) {
                player.sendMessage("You already have a hydration level container.");
            } else {
                // Give the player a hydration level container starting at 10000
                player.getPersistentDataContainer().set(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER, 3500);

                player.sendMessage("You have been given a hydration level container with a value of 3500.");
            }
        } else {
            sender.sendMessage("This command can only be executed by a player.");
        }

        return true;
    }

    private boolean hasDataContainer(Player player) {
        // Check if the player has a hydration level container
        return player.getPersistentDataContainer().has(DatapackKey.HYDRATION_LEVEL_KEY, PersistentDataType.INTEGER);
    }
}

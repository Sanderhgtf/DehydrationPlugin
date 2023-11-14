package me.sander.dehydrationplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class RemoveDataContainerCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Check if the player has a hydration level container
            if (hasDataContainer(player)) {
                // Remove the hydration level from the player's data container
                player.getPersistentDataContainer().remove(DatapackKey.HYDRATION_LEVEL_KEY);

                player.sendMessage("Your hydration level has been removed.");
            } else {
                player.sendMessage("You don't have a hydration level container.");
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

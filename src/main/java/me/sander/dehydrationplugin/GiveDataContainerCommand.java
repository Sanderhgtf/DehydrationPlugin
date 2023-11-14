package me.sander.dehydrationplugin;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class GiveDataContainerCommand implements CommandExecutor {

    private static final NamespacedKey DATA_KEY = new NamespacedKey(DehydrationPlugin.getPlugin(DehydrationPlugin.class), "data_key");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be executed by a player.");
            return true;
        }

        Player targetPlayer = (Player) sender;

        // Give the target player a data container
        giveDataContainer(targetPlayer);

        sender.sendMessage("Data container given to " + targetPlayer.getName());
        return true;
    }

    private void giveDataContainer(Player player) {
        // Create a persistent data container
        player.getPersistentDataContainer().set(DATA_KEY, PersistentDataType.INTEGER, 0);
    }
}

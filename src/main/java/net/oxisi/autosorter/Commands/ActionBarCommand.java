package net.oxisi.autosorter.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionBarCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        // Check if the command has at least one argument
        if (args.length < 1) {
            sender.sendMessage("Incorrect usage. Please provide a text to display.");
            return true;
        }

        // Join all arguments into a single string
        String text = String.join(" ", args);

        // Display the text in the action bar for all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendActionBar(ChatColor.translateAlternateColorCodes('&', text));
        }

        return true;
    }
}

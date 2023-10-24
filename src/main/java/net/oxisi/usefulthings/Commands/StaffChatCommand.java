package net.oxisi.usefulthings.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /st <message>");
            return true;
        }

        String message = String.join(" ", args);

        // Format the message, you can change this to whatever you like
        String formattedMessage = ChatColor.DARK_GRAY + "[Staff] " + ChatColor.GRAY + player.getName() + ": " + message;

        // Send it to all online players with the right permission
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("usefulthings.sc"))
                .forEach(p -> p.sendMessage(formattedMessage));

        return true;
    }
}

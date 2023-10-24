package net.oxisi.usefulthings.Commands;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Optional;
import java.time.Duration;

public class RankTimeLeftCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        if (user == null) {
            player.sendMessage("Error fetching your rank data.");
            return true;
        }

        String primaryGroup = user.getPrimaryGroup();
        Optional<Node> primaryGroupNodeOptional = user.getNodes().stream()
                .filter(node -> node instanceof InheritanceNode && ((InheritanceNode) node).getGroupName().equals(primaryGroup))
                .findFirst();

        if (primaryGroupNodeOptional.isPresent()) {
            Node primaryGroupNode = primaryGroupNodeOptional.get();
            if (primaryGroupNode.hasExpiry()) {
                Instant expiryTime = primaryGroupNode.getExpiry();
                Instant now = Instant.now();
                Duration duration = Duration.between(now, expiryTime);

                long totalSeconds = duration.getSeconds();
                long weeks = totalSeconds / (7 * 24 * 60 * 60);
                totalSeconds %= (7 * 24 * 60 * 60);
                long days = totalSeconds / (24 * 60 * 60);
                totalSeconds %= (24 * 60 * 60);
                long hours = totalSeconds / (60 * 60);
                totalSeconds %= (60 * 60);
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;

                if (weeks <= 0 && days <= 0 && hours <= 0 && minutes <= 0 && seconds <= 0) {
                    player.sendMessage("Your rank has expired.");
                } else {
                    StringBuilder timeLeftMessage = new StringBuilder(ChatColor.GREEN + "Time left for your rank: ");
                    if (weeks > 0) timeLeftMessage.append(ChatColor.GREEN).append(weeks).append(" weeks, ").append(ChatColor.RESET).append(" ");
                    if (days > 0) timeLeftMessage.append(ChatColor.GREEN).append(days).append(" days, ").append(ChatColor.RESET).append(" ");
                    if (hours > 0) timeLeftMessage.append(ChatColor.GREEN).append(hours).append(" hours, ").append(ChatColor.RESET).append(" ");
                    if (minutes > 0) timeLeftMessage.append(ChatColor.GREEN).append(minutes).append(" minutes, ").append(ChatColor.RESET).append(" ");
                    if (seconds > 0) timeLeftMessage.append(ChatColor.GREEN).append(seconds).append(" seconds.").append(ChatColor.RESET).append(" ");


                    player.sendMessage(timeLeftMessage.toString());
                }
            } else {
                player.sendMessage("Your rank does not have an expiry time.");
            }
        } else {
            player.sendMessage("You do not have a primary rank.");
        }

        return true;
    }
}


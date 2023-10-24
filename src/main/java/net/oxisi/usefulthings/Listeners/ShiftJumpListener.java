package net.oxisi.usefulthings.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class ShiftJumpListener implements Listener {
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerID = player.getUniqueId();

        // Check if the player is jumping and shifting
        if (player.isSneaking() && event.getFrom().getY() < event.getTo().getY()) {
            // Check if 30 seconds have passed since the player last used the feature
            if (cooldowns.containsKey(playerID) && cooldowns.get(playerID) > System.currentTimeMillis()) {
                // If not, send a message and return
//                ((Player) player).sendMessage(ChatColor.RED + "You can't use this feature again for " + ((cooldowns.get(playerID) - System.currentTimeMillis()) / 1000) + " seconds.");
                return;
            }

            // Get the block the player is looking at
            Block pointedBlock = player.getTargetBlock(null, 100);
            // Send the coordinates of the pointed block to the player
            player.sendMessage(ChatColor.GREEN + "\nX: " + pointedBlock.getLocation().getX() + "\nY: " + pointedBlock.getLocation().getY() + "\nZ: " + pointedBlock.getLocation().getZ() + "\n");

            // Set a 30 second cooldown for the player
            cooldowns.put(playerID, System.currentTimeMillis() + (30 * 1000));
        }
    }
}

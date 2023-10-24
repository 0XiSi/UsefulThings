package net.oxisi.usefulthings.Commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SandCastle implements CommandExecutor {

    private final Plugin plugin;

    public SandCastle(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {

            int playerX = player.getLocation().getBlockX();
            int playerY = player.getLocation().getBlockY();
            int playerZ = player.getLocation().getBlockZ();

            new BukkitRunnable() {
                int i = 0;
                @Override
                public void run() {
                    for (int x = playerX - i; x <= playerX + i; x++) {
                        for (int z = playerZ - i; z <= playerZ + i; z++) {
                            Location loc = new Location(player.getWorld(), x, playerY - 1, z);
                            if (loc.getBlock().getType() != Material.SAND) {
                                loc.getBlock().setType(Material.SAND);
                            }
                        }
                    }

                    if (i < 3) {
                        for (int y = playerY; y <= playerY + i; y++) {
                            int newy = y-1;
                            Location loc1 = new Location(player.getWorld(), playerX - 2, newy, playerZ - 2);
                            Location loc2 = new Location(player.getWorld(), playerX + 2, newy, playerZ - 2);
                            Location loc3 = new Location(player.getWorld(), playerX - 2, newy, playerZ + 2);
                            Location loc4 = new Location(player.getWorld(), playerX + 2, newy, playerZ + 2);
                            loc1.getBlock().setType(Material.SAND);
                            loc2.getBlock().setType(Material.SAND);
                            loc3.getBlock().setType(Material.SAND);
                            loc4.getBlock().setType(Material.SAND);
                        }
                    }
                    if (i >= 1 && i < 3) {
                        for (int x = playerX - 2; x <= playerX + 2; x++) {
                            for (int z = playerZ - 2; z <= playerZ + 2; z++) {
                                if (x == playerX - 2 || x == playerX + 2 || z == playerZ - 2 || z == playerZ + 2) {
                                    Location loc = new Location(player.getWorld(), x, playerY, z);
                                    if (loc.getBlock().getType() != Material.SAND) {
                                        loc.getBlock().setType(Material.SAND);
                                    }
                                }
                            }
                        }

                        // Place a sand block in the middle of each wall
                        Location loc1 = new Location(player.getWorld(), playerX, playerY+1, playerZ - 2);
                        Location loc2 = new Location(player.getWorld(), playerX, playerY+1, playerZ + 2);
                        Location loc3 = new Location(player.getWorld(), playerX - 2, playerY+1, playerZ);
                        Location loc4 = new Location(player.getWorld(), playerX + 2, playerY+1, playerZ);
                        loc1.getBlock().setType(Material.SAND);
                        loc2.getBlock().setType(Material.SAND);
                        loc3.getBlock().setType(Material.SAND);
                        loc4.getBlock().setType(Material.SAND);
                    }

                    i++;
                    if (i >= 5) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 second delay
        }
        return true;
    }
}

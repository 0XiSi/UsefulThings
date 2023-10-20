package net.oxisi.autosorter.Events;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Duration;

public class PlayerJoinCountdown implements Listener {
    public static int num;
    public static boolean canMove = false;
    private final JavaPlugin plugin;

    public PlayerJoinCountdown(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!event.getPlayer().hasPlayedBefore()) {
            num = 6;
            Component firstTimeJoinedMessage = Component.text(
                    ChatColor.GREEN + player.getName() + " Has joined for the first time!");
            event.joinMessage(firstTimeJoinedMessage);
            new BukkitRunnable() {

                @Override
                public void run() {
                    num -= 1;
                    showMyTitleWithDurations(player, "", String.valueOf(num) + ChatColor.RED + " Seconds to start playing!");
                    if (num <= 0) {
                        this.cancel();
                        showMyTitleWithDurations(player, "", ChatColor.GREEN + "Start your great adventure");
                        canMove = true;
                    }
                }
            }.runTaskTimer(plugin, 0, 20);


        }
        else {
            showMyTitleWithDurations(player, ChatColor.GREEN + player.getName(), ChatColor.GREEN + "Welcome back");
            canMove = true;
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!canMove) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        if (!canMove) {
            breakEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent itemEvent) {
        if (!canMove && itemEvent.getEntity() instanceof Player) {
            itemEvent.setCancelled(true);
        }
    }

    public void showMyTitleWithDurations(final @NonNull Player player,String titleText , String subtitle) {
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3000), Duration.ofMillis(1000));
        // Using the times object this title will use 500ms to fade in, stay on screen for 3000ms and then fade out for 1000ms
        final Title title = Title.title(Component.text(titleText), Component.text(
                 String.valueOf(subtitle)), times);
        // Send the title, you can also use Audience#clearTitle() to remove the title at any time
        player.showTitle(title);
    }
}

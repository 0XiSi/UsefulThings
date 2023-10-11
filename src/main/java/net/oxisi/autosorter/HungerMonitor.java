package net.oxisi.autosorter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HungerMonitor implements Runnable {

    private JavaPlugin plugin;

    public HungerMonitor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        // Loop through all online players
        for (Player player : Bukkit.getOnlinePlayers()) {
            // Check if the player's food level is 3 or less
            if (player.getFoodLevel() <= 3) {
                // Apply the nausea effect
                applyNausea(player);
            }
        }
    }

    private void applyNausea(Player player) {
        // Create a potion effect of nausea for 200 ticks (10 seconds)
        PotionEffect nausea = new PotionEffect(PotionEffectType.CONFUSION, 200, 0);

        // Apply the potion effect to the player
        player.addPotionEffect(nausea);
    }
}

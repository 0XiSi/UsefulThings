package net.oxisi.autosorter;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArmorStandCommand implements CommandExecutor {

    private final AutoSorter plugin;

    public ArmorStandCommand(AutoSorter plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ArmorStand armorStand = player.getWorld().spawn(player.getLocation(), ArmorStand.class);
            armorStand.setHelmet(player.getInventory().getHelmet());
            armorStand.setChestplate(player.getInventory().getChestplate());
            armorStand.setLeggings(player.getInventory().getLeggings());
            armorStand.setBoots(player.getInventory().getBoots());
            armorStand.setVisible(false);
            armorStand.setBasePlate(false);
            armorStand.setInvulnerable(true);
            armorStand.setGravity(false);
            armorStand.setCanPickupItems(false);

//            player.sendMessage(ChatColor.AQUA + "Your clone has been created successfully and it will be deleted in 30 seconds.");

            TextComponent message = new TextComponent("§bYour clone has been created successfully and it will be deleted in 30 seconds.");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.example.com"));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Hover text")));
            player.spigot().sendMessage(message);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    armorStand.remove();
                }
            }, 600L); // This will remove the armor stand after 30 seconds (600 ticks, as 20 ticks = 1 second)
        }
        return true;
    }
}

package net.oxisi.autosorter.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OpenInventoryCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }

        if (!player.hasPermission("autosorter.openinv")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Please specify a player.");
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage("Player not found.");
            return false;
        }

        sender.sendMessage(ChatColor.AQUA + "Openning " + target.getName() + "'s Inventory");
        Inventory inv = Bukkit.createInventory(null, 54, target.getName() + "'s Inventory");


        for (int i = 9; i < target.getInventory().getSize(); i++) {
            inv.setItem(i, target.getInventory().getItem(i));
        }

        // Add hotbar items to the last row
        for (int i = 0; i < 9; i++) {
            inv.setItem(i + 45, target.getInventory().getItem(i));
        }

        // off-hand item
        inv.setItem(0, target.getInventory().getItemInOffHand());

        // armor items
        ItemStack[] armor = target.getInventory().getArmorContents();
        inv.setItem(1, armor[0]); // Boots
        inv.setItem(2, armor[1]); // Leggings
        inv.setItem(3, armor[2]); // Chestplate
        inv.setItem(4, armor[3]); // Helmet

        // ------------------------------------------------------------------------------
        ItemStack redGlassPane = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        for (int i = 36; i < 45; i++) {
            inv.setItem(i, redGlassPane);
        }
        // ------------------------------------------------------------------------------

        player.openInventory(inv);

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().endsWith("'s Inventory")) {
            event.setCancelled(true);
        }
    }
}

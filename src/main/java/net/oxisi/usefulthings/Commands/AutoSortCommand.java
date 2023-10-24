package net.oxisi.usefulthings.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static org.bukkit.Color.AQUA;

public class AutoSortCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            ItemStack[] inventory = player.getInventory().getContents();
            ItemStack[] itemsExcludingHotbarAndArmor = new ItemStack[inventory.length - 9 - 5];
            System.arraycopy(inventory, 9, itemsExcludingHotbarAndArmor, 0, inventory.length - 9 - 5);

            Arrays.sort(itemsExcludingHotbarAndArmor, (item1, item2) -> {
                if (item1 == null && item2 == null) {
                    return 0;
                }

                if (item1 == null) {
                    return 1;
                }

                if (item2 == null) {
                    return -1;
                }

                return item1.getType().name().compareTo(item2.getType().name());
            });

            System.arraycopy(itemsExcludingHotbarAndArmor, 0, inventory, 9, itemsExcludingHotbarAndArmor.length);

            player.getInventory().setContents(inventory);



            player.sendMessage(ChatColor.AQUA + "Your inventory has been sorted!");
            return true;
        }

        return false;
    }
}

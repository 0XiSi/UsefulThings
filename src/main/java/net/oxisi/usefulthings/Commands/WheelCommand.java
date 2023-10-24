package net.oxisi.usefulthings.Commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WheelCommand implements CommandExecutor, Listener {

    private final JavaPlugin plugin;

    public WheelCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    static ArmorStand startButton;
    private final List<ArmorStand> rotatingArmorStands = new ArrayList<>();
    private boolean isSpinning = false;
    private final double rotationSpeed = 0.05;

    public static ItemStack leatherHelmet;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can run this command.");
            return true;
        }

        Location baseLoc = player.getLocation();
        Material targetMaterial = Material.BLACK_CONCRETE;

        int[] offsets = {-1, 0, 1};

        for (int x : offsets) {
            for (int z : offsets) {
                player.getLocation().add(x, 1, z).getBlock().setType(Material.AIR);
                player.getLocation().add(x, 0, z).getBlock().setType(Material.AIR);

                baseLoc.add(x, -1, z).getBlock().setType(targetMaterial);
                baseLoc.subtract(x, -1, z);
            }
        }

        World world = player.getWorld();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        Location blockLocation = new Location(world, x, y, z);

        Location spawnLocation = blockLocation.add(0.5, -1.7, 0.5);
        startButton = world.spawn(spawnLocation, ArmorStand.class);

        startButton.setCustomName("InteractToRemove");

        colorArmor(Material.LEATHER_HELMET, Color.LIME);

        startButton.getEquipment().setHelmet(leatherHelmet);
        startButton.setVisible(false);
        startButton.setBasePlate(false);
        startButton.setGravity(false);

        // Set the center point for rotation
        double centerX = x + 0.5;
        double centerZ = z + 0.5;

        return true;
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        ArmorStand armorStand = event.getRightClicked();
        Bukkit.getLogger().info(armorStand.getCustomName());
        if ("InteractToRemove".equals(armorStand.getCustomName())) {
            colorArmor(Material.LEATHER_HELMET, Color.RED);
            startButton.getEquipment().setHelmet(leatherHelmet);
            Bukkit.getLogger().info("E");
            if (!isSpinning) {
                Bukkit.getLogger().info("R");
                startSpinning(event.getPlayer());
            } else {
                Bukkit.getLogger().info("U");
                stopSpinning();
            }
            event.setCancelled(true);
        }
        if ("RotatingStand".equals(armorStand.getCustomName())){
            event.setCancelled(true);
        }
    }

    private void startSpinning(Player player) {
        isSpinning = true;

        // Calculate initial angles for evenly spaced rotating stands
        int numRotatingStands = 3;
        double angleIncrement = 2 * Math.PI / numRotatingStands;
        double currentAngle = 0;

        double radius = 1.5; // Adjust the radius as needed
        double yOffset = 1.0; // Adjust the height above the startButton

        for (int i = 0; i < numRotatingStands; i++) {
            double x = startButton.getLocation().getX() + Math.cos(currentAngle) * radius;
            double z = startButton.getLocation().getZ() + Math.sin(currentAngle) * radius;
            double y = startButton.getLocation().getY() + yOffset; // Add yOffset

            Location spawnLocation = new Location(player.getWorld(), x, y, z);

            ArmorStand rotatingArmorStand = player.getWorld().spawn(spawnLocation, ArmorStand.class);

            rotatingArmorStand.setCustomNameVisible(false);
            rotatingArmorStand.setCustomName("RotatingStand");
            rotatingArmorStand.setVisible(false);
            rotatingArmorStand.setBasePlate(false);
            colorArmor(Material.LEATHER_HELMET, Color.YELLOW);
            rotatingArmorStand.getEquipment().setHelmet(leatherHelmet);

            rotatingArmorStands.add(rotatingArmorStand);

            currentAngle += angleIncrement;
        }

        // Define a duration (in ticks) for how long the rotation should continue
        int rotationDuration = 160; // Adjust as needed

        new BukkitRunnable() {
            double angle = 0;
            int ticksElapsed = 0;

            @Override
            public void run() {
                if (ticksElapsed >= rotationDuration) {
                    cancel();
                    isSpinning = false; // Set isSpinning to false when rotation ends

                    // Remove each armor stand individually
                    for (ArmorStand rotatingArmorStand : rotatingArmorStands) {
                        rotatingArmorStand.remove();
                    }
                    rotatingArmorStands.clear();

                    startButton.remove();
                    // Drop an enchanted golden apple
                    Location dropLocation = startButton.getLocation().add(0, 2, 0); // Adjust the height as needed
                    ItemStack enchantedGoldenApple = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);

                    World world = startButton.getWorld();
                    double x = startButton.getLocation().getX();
                    double y = startButton.getLocation().getY();
                    double z = startButton.getLocation().getZ();
                    world.spawnParticle(Particle.TOTEM, dropLocation, 255);

                    player.getWorld().dropItem(dropLocation, enchantedGoldenApple);

                    return;
                }

                angle += rotationSpeed;

                for (ArmorStand rotatingArmorStand : rotatingArmorStands) {
                    Location location = rotatingArmorStand.getLocation();
                    double x = location.getX() - startButton.getLocation().getX();
                    double z = location.getZ() - startButton.getLocation().getZ();

                    double newX = x * Math.cos(angle) - z * Math.sin(angle) + startButton.getLocation().getX();
                    double newZ = x * Math.sin(angle) + z * Math.cos(angle) + startButton.getLocation().getZ();

                    location.setX(newX);
                    location.setZ(newZ);

                    // Set the armor stand's body rotation to face outward from the circle
                    rotatingArmorStand.setBodyPose(rotatingArmorStand.getBodyPose().setX((angle + Math.PI / 2) % (Math.PI * 2)));

                    rotatingArmorStand.teleport(location);
                }

                ticksElapsed++;
            }
        }.runTaskTimer(plugin, 0, 1);
    }


    private void stopSpinning() {
        isSpinning = false;

        // Remove the rotating Armor Stands
        for (ArmorStand rotatingArmorStand : rotatingArmorStands) {
            rotatingArmorStand.remove();
        }
        rotatingArmorStands.clear();
    }
    public final void colorArmor(Material armor, Color color){
        leatherHelmet = new ItemStack(armor);
        LeatherArmorMeta meta = (LeatherArmorMeta) leatherHelmet.getItemMeta();
        meta.setColor(color);
        leatherHelmet.setItemMeta(meta);
    }
}
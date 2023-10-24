package net.oxisi.usefulthings.Listeners;

import net.oxisi.usefulthings.UsefulThings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityDeathListener implements Listener {

    private final UsefulThings plugin;

    public EntityDeathListener(UsefulThings plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            return;
        }
        Location loc = event.getEntity().getLocation().add(0, 0.5, 0);

        ArmorStand smallStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        smallStand.setVisible(false);
        smallStand.setGravity(false);
        smallStand.setSmall(true);

        ArmorStand normalStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
        normalStand.setVisible(false);
        normalStand.setGravity(false);

        ItemStack glass = new ItemStack(Material.WHITE_STAINED_GLASS);
//        smallStand.setHelmet(glass);
        new BukkitRunnable() {
            double angle = 0;
            int tick = 0;

            @Override
            public void run() {
                angle += Math.PI / 20;
                tick++;

                if (angle > Math.PI * 2) {
                    angle -= Math.PI * 2;
                }

                Location smallLoc = smallStand.getLocation();
                Location normalLoc = normalStand.getLocation();
                smallLoc.setYaw((float) Math.toDegrees(angle));
                normalLoc.setYaw((float) Math.toDegrees(angle));

                smallLoc.setPitch(45);
                normalLoc.setPitch(45);

                spawnSoulParticles(smallLoc, 0.5, 5, 0.5);

                if (tick <= 30) {
                    smallLoc.add(0, 0.02, 0);
                    smallStand.teleport(smallLoc);
                }

                if (tick == 30) {
//                    normalStand.setHelmet(glass);
                    smallStand.remove();
                }

                if (tick > 32) {
                    normalLoc.add(0, 0.02, 0);
                    normalStand.teleport(normalLoc);
                }

                if (tick >= 62) {
                    normalStand.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void spawnSoulParticles(Location location, double radius, int count, double speed) {
        World world = location.getWorld();
        double increment = (2 * Math.PI) / count;
        for (int i = 0; i < count; i++) {
            double angle = i * increment;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            location.add(0, speed, 0); // Increase y-coordinate for faster upward movement
            world.spawnParticle(Particle.SOUL, location.add(x, 0, z), 0);
            location.subtract(x, speed, z); // Reset the location for the next particle
        }
    }

}
package net.oxisi.autosorter;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.BoundingBox;

import static org.bukkit.Tag.LEAVES;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        World world = block.getWorld();

        // Get the color for the block type
        Color color = getColorForBlock(block);

        // Create a particle effect with the color
        Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);

        // Get the block's bounding box
        BoundingBox box = block.getBoundingBox();

        // Calculate the number of particles to spawn (you can adjust this as needed)
        int particles = 100;

        // Spawn particles around the edges of the block
        for (int i = 0; i < particles; i++) {
            double x = box.getMinX() + (box.getMaxX() - box.getMinX()) * Math.random();
            double y = box.getMinY() + (box.getMaxY() - box.getMinY()) * Math.random();
            double z = box.getMinZ() + (box.getMaxZ() - box.getMinZ()) * Math.random();

            Location particleLocation = new Location(world, x, y, z);
            world.spawnParticle(Particle.REDSTONE, particleLocation, 1, dustOptions);
        }
    }


    private Color getColorForBlock(Block block) {
        Material type = block.getType();

        return switch (type) {
            case DIAMOND_BLOCK, DIAMOND_ORE -> Color.AQUA;
            case GOLD_BLOCK, GOLD_ORE -> Color.YELLOW;
            case EMERALD_BLOCK, EMERALD_ORE, GRASS_BLOCK, OAK_LEAVES, DARK_OAK_LEAVES, BIRCH_LEAVES, SPRUCE_LEAVES, AZALEA_LEAVES, ACACIA_LEAVES, JUNGLE_LEAVES, GRASS, TALL_GRASS, SUGAR_CANE ->
                    Color.GREEN;
            case IRON_BLOCK, IRON_ORE -> Color.SILVER;
            case DIRT -> Color.MAROON;
            case STONE -> Color.GRAY;

            default -> Color.WHITE;
        };
    }

}

package net.oxisi.autosorter;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class DisplayEntityCommand implements CommandExecutor, TabCompleter {
    private static final List<String> COLOR_NAMES = Arrays.asList(
            "BLACK", "DARK_BLUE", "DARK_GREEN", "DARK_AQUA", "DARK_RED",
            "DARK_PURPLE", "GOLD", "GRAY", "DARK_GRAY", "BLUE"
            // ... add other color names as needed
    );

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");

            return true;
        }

        try {
            // 1: Display items
            if (args[0].equals("item")) {
                ItemDisplay item = player.getWorld().spawn(player.getLocation().add(0, 1, 0), ItemDisplay.class);
                item.setItemStack(new ItemStack(Material.DIAMOND));

                Transformation transformation = item.getTransformation();

                transformation.getScale().set(2D);

                //transformation.getLeftRotation().x = 1; // 1 to -1, forward/backward lay
                //transformation.getLeftRotation().y = 0.5F; // 1 to -1, horizontal rotation
                //transformation.getLeftRotation().z = -1F; // 1 to -1, right/left tilt

                item.setTransformation(transformation);

                //item.setViewRange(0.1F); /* 0.1 = 16 blocks */
                //item.setShadowRadius(0.3F); // 1 = 1 block
                //item.setShadowRadius(1F);
                //item.setShadowStrength(5F); // >= 5F = "black hole"

                //item.setDisplayWidth(50F);
                //item.setDisplayHeight(50F);

                item.setBillboard(Billboard.FIXED); // auto-rotate

                item.setGlowColorOverride(Color.RED); // only works for scoreboard

                item.setBrightness(new Display.Brightness(15 , 15)); // 0-15, will override auto brightness
            }

            // 2: Blocks
            if (args[0].equals("block")) {
                BlockDisplay block = player.getWorld().spawn(player.getLocation().add(0, 1, 0), BlockDisplay.class);
                block.setBlock(Bukkit.createBlockData(Material.DIAMOND_BLOCK));

                Transformation transformation = block.getTransformation();
                transformation.getScale().set(2D);

                block.setTransformation(transformation);
            }

            // 3: Texts
            if (args[0].equals("text")) {
                TextDisplay text = player.getWorld().spawn(player.getLocation().add(0, 1, 0), TextDisplay.class);

                String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                message = message.replace("\\n", "\n"); // Replace string "\n" with actual newline character
                text.setText(ChatColor.translateAlternateColorCodes('&', message));

                player.sendMessage(centerMessage(message));

//                text.setLineWidth(50);
                text.setBillboard(Billboard.CENTER);

                Color bgColor = stringToColor(args[1]);
                text.setBackgroundColor(Objects.requireNonNullElseGet(bgColor, () -> Color.BLACK.setAlpha(1)));
                text.setTextOpacity(Byte.MAX_VALUE); // transparent
            }

            if (args[0].equals("del")) {
                World world = player.getWorld();  // or any other method to get the world
                String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
                despawnTextDisplayWithContent(world, message);
            }

        } catch (Throwable t) {
            sender.sendMessage("Error: " + t.getMessage());
            t.printStackTrace();
        }

        return true;
    }

    public Color stringToColor(String colorName) {
        return switch (colorName.toUpperCase()) {
            case "BLACK" -> Color.BLACK;
            case "DARK_BLUE" -> Color.fromRGB(0, 0, 170);
            case "DARK_GREEN" -> Color.fromRGB(0, 170, 0);
            case "DARK_AQUA" -> Color.fromRGB(0, 170, 170);
            case "DARK_RED" -> Color.fromRGB(170, 0, 0);
            case "DARK_PURPLE" -> Color.fromRGB(170, 0, 170);
            case "GOLD" -> Color.fromRGB(255, 170, 0);
            case "GRAY" -> Color.fromRGB(170, 170, 170);
            case "DARK_GRAY" -> Color.fromRGB(85, 85, 85);
            case "BLUE" -> Color.fromRGB(85, 85, 255);
            // ... add other colors as needed
            default -> null; // or a default color like Color.WHITE
        };
    }
    public static String centerMessage(String message) {
        int maxWidth = 53;  // this can vary
        int spaces = (maxWidth - ChatColor.stripColor(message).length()) / 2;

        // Using StringBuilder for efficiency
        StringBuilder centered = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            centered.append(" ");
        }
        centered.append(message);

        return centered.toString();
    }
    public void despawnTextDisplayWithContent(World world, String targetText) {
        // Retrieve all entities in the world
        for (Entity entity : world.getEntities()) {

            // Check if entity is a TextDisplay
            if (entity instanceof TextDisplay) {
                TextDisplay textDisplay = (TextDisplay) entity;

                // Check if the text matches the target text
                if (textDisplay.getText().equals(targetText)) {

                    // Despawn or remove the TextDisplay entity
                    entity.remove();
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> proposals = new ArrayList<>();

        if (args.length == 1) {  // Suggesting for the first argument
            proposals.add("text");
            proposals.add("block");
            proposals.add("item");
            proposals.add("del");
            // ... add other completions for the first argument ...

            StringUtil.copyPartialMatches(args[0], proposals, completions);
        }
        else if (args.length == 2) {  // Suggesting for the second argument based on the first argument
            if ("text".equalsIgnoreCase(args[0])) {
                // If the first argument is "text", suggest colors for the second argument
                StringUtil.copyPartialMatches(args[1], COLOR_NAMES, completions);
            }
            // ... add other conditions based on the first argument ...
        }
        // You can add more else-if blocks for further arguments if necessary

        Collections.sort(completions);
        return completions;
    }
}

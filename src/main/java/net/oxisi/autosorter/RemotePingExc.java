package net.oxisi.autosorter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class RPEPlugin extends JavaPlugin implements CommandExecutor {
    private long lastExecutionTime = 0;

    @Override
    public void onEnable() {
        getCommand("rpe").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("rpe")) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastExecutionTime >= 30000) {
                lastExecutionTime = currentTime;
                String message = String.join(" ", args);
                sendToDiscord(message);
                sender.sendMessage("Message sent to Discord!");
            } else {
                sender.sendMessage("Please wait for the cooldown!");
            }
        }
        return true;
    }

    private void sendToDiscord(String message) {

        String webhookUrl = "https://discord.com/api/webhooks/1161934420941492384/8jEjxrLyGoVpxIrahNT4Fq2Pio2P__s0ICLP_qfIy_KiaBjlwOSuLOVqwLKn4fvqxl6r";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(webhookUrl).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.getOutputStream().write(("{\"content\":\"" + message + "\"}").getBytes());
            connection.getInputStream().close();  
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

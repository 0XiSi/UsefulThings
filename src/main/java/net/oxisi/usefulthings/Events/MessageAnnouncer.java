package net.oxisi.usefulthings.Events;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageAnnouncer implements Runnable {

    ChatColor color = ChatColor.of("#5555ff");
    private String[] messages = {
            "\n" +
                    "                             &b&lDiscord\n" +
                    "        &bClick to join our &9Discord &r&bto buy ranks.\n",
            "\n" +
                    "                             &b&lDiscord\n" +
                    "    &bClick to join our &9Discord &r&bto report other players.\n"
    };

    private String[] urls = {
            "https://discord.gg/zXn3ZfjmF7",
            "https://discord.gg/zXn3ZfjmF7"
    };
    private String[] hoverTexts = {
            "&bClick to join our &9Discord",
            "&bClick to join our &9Discord"
    };
    private int index = 0;

    @Override
    public void run() {
        String messageText = ChatColor.translateAlternateColorCodes('&', messages[index]);
        String url = urls[index];
        String hoverText = ChatColor.translateAlternateColorCodes('&', hoverTexts[index]);


        TextComponent message = new TextComponent(messageText);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            player.spigot().sendMessage(message);
        }

        index = (index + 1) % messages.length; // Loop back to the first message when we reach the end
    }
}

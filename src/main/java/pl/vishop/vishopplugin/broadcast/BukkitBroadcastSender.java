package pl.vishop.vishopplugin.broadcast;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class BukkitBroadcastSender implements BroadcastSender {

    @Override
    public void broadcastMessages(List<String> rawMessages) {
        rawMessages.stream()
                .map(message -> ChatColor.translateAlternateColorCodes('&', message))
                .forEach(Bukkit::broadcastMessage);
    }

}

package pl.vishop.vishopplugin.broadcast;

import de.themoep.minedown.MineDown;
import org.bukkit.Bukkit;

import java.util.List;

public class ModernBroadcastSender implements BroadcastSender{

    @Override
    public void broadcastMessages(List<String> rawMessages) {
        rawMessages.stream()
                .map(MineDown::parse)
                .forEach(message -> Bukkit.spigot().broadcast(message));
    }

}

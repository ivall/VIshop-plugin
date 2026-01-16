package pl.vishop.plugin.hytale;

import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.universe.Universe;

import com.hypixel.hytale.server.core.console.ConsoleSender;
import okhttp3.OkHttpClient;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.logger.ViShopLogger;
import pl.vishop.plugin.order.OrderTask;

public class HytaleOrderTask extends OrderTask {

    public HytaleOrderTask(final OkHttpClient httpClient, final Config config, final ViShopLogger logger) {
        super(httpClient, config, logger);
    }

    @Override
    public boolean isPlayerOnline(final String playerName) {
        return Universe.get().getPlayerByUsername(playerName, NameMatching.EXACT) != null;
    }

    @Override
    public void executeCommand(final String command) {
        CommandManager.get().handleCommand(ConsoleSender.INSTANCE, command);
    }

}
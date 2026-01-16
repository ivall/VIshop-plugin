package pl.vishop.plugin.hytale;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.plugin.PluginManager;
import okhttp3.OkHttpClient;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.config.EmptyConfigFieldException;
import pl.vishop.plugin.logger.ViShopLogger;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class HytaleViShopPlugin extends JavaPlugin {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    final ViShopLogger logger = new HytaleVishopLogger(getLogger());

    public HytaleViShopPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        Config config;
        try {
            config = new Config(new HytaleConfigLoader(getDataDirectory()));
        } catch (EmptyConfigFieldException | IOException exception) {
            logger.error(exception.getMessage());
            PluginManager.get().unload(this.getIdentifier());
            return;
        }

        HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
                new HytaleOrderTask(this.httpClient, config, logger),
                0, 30, TimeUnit.SECONDS
        );
    }

}

/*
 * Copyright (C) 2022 VIshop-plugin Contributors
 * https://github.com/ivall/VIshop-plugin/graphs/contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.vishop.plugin.hytale;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import okhttp3.OkHttpClient;
import org.spongepowered.configurate.ConfigurationNode;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.config.EmptyConfigFieldException;
import pl.vishop.plugin.logger.ViShopLogger;
import pl.vishop.plugin.resource.ResourceLoader;
import pl.vishop.plugin.resource.ResourceLoaderException;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

public class HytaleViShopPlugin extends JavaPlugin {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    final ViShopLogger logger = new HytaleViShopLogger(getLogger());

    public HytaleViShopPlugin(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        final ResourceLoader<ConfigurationNode> resourceLoader = new HytaleResourceLoader(this.getClass(), getDataDirectory());
        try {
            if (resourceLoader.saveDefault("config.yml")) {
                this.logger.info("Domyślny plik config.yml zapisany, skonfiguruj go i zrestartuj serwer");
                return;
            }

            try {
                final ConfigurationNode cfgFile = resourceLoader.load("config.yml");
                final Config config = new Config(new HytaleConfigLoader(cfgFile));

                HytaleServer.SCHEDULED_EXECUTOR.scheduleAtFixedRate(
                        new HytaleOrderTask(this.httpClient, config, logger),
                        0, config.taskInterval.getSeconds(), TimeUnit.SECONDS
                );

            } catch (final EmptyConfigFieldException exception) {
                this.logger.error(exception.getMessage());
            }
        } catch (final ResourceLoaderException exception) {
            this.logger.error(exception.getReason().getMessage("config.yml"));
            this.logger.error("Przyczyna: " + exception.getCause().getMessage());
        }
    }

    @Override
    protected void shutdown() {
        HytaleServer.SCHEDULED_EXECUTOR.shutdown();
        this.httpClient.dispatcher().executorService().shutdown();
    }

}

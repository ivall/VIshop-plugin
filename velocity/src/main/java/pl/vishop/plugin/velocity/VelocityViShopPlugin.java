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

package pl.vishop.plugin.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import ninja.leaping.configurate.ConfigurationNode;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.config.EmptyConfigFieldException;
import pl.vishop.plugin.logger.ViShopLogger;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = "vishop",
        name = "ViShopPlugin",
        version = "2.4",
        description = "Wykonuj zamówienia ze swojego sklepu ViShop",
        url = "https://vishop.pl/",
        authors = "VIshop-plugin Contributors"
)
public class VelocityViShopPlugin {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .callTimeout(5, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(5, 30, TimeUnit.SECONDS))
            .build();
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;

    private ScheduledTask orderTask;

    @Inject
    public VelocityViShopPlugin(final ProxyServer proxy, final Logger logger, @DataDirectory final Path dataDirectory) {
        this.proxy = proxy;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInit(final ProxyInitializeEvent event) {
        try {
            final Config config = ConfigManager.create(Config.class, it -> it
                    .withBindFile(new File(dataDirectory.toFile(), "config.yml"))
                    .withConfigurer(new YamlSnakeYamlConfigurer())
                    .saveDefaults()
                    .load(true));
            final ViShopLogger viShopLogger = new VelocityViShopLogger(this.logger);

            this.orderTask = this.proxy.getScheduler()
                    .buildTask(this, new VelocityOrderTask(this.proxy, this.httpClient, config, viShopLogger))
                    .repeat(config.taskInterval, TimeUnit.SECONDS)
                    .schedule();
        } catch (final OkaeriException exception) {
            this.logger.error(exception.getCause().getMessage());
        }
    }

    @Subscribe
    public void onProxyShutdown(final ProxyShutdownEvent event) {
        if (this.orderTask != null) {
            this.orderTask.cancel();
        }

        this.httpClient.dispatcher().executorService().shutdown();
    }

}

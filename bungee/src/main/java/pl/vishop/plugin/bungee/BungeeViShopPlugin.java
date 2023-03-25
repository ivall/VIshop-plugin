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

package pl.vishop.plugin.bungee;

import java.io.File;
import java.util.concurrent.TimeUnit;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.config.EmptyConfigFieldException;
import pl.vishop.plugin.logger.ViShopLogger;
import pl.vishop.plugin.resource.ResourceLoader;
import pl.vishop.plugin.resource.ResourceLoaderException;

public class BungeeViShopPlugin extends Plugin {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .callTimeout(5, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(5, 30, TimeUnit.SECONDS))
            .build();

    @Override
    public void onEnable() {
            try {
                final Config config = ConfigManager.create(Config.class, it -> it
                        .withBindFile(new File(getDataFolder(), "config.yml"))
                        .withConfigurer(new YamlSnakeYamlConfigurer())
                        .saveDefaults()
                        .load(true));
                final ViShopLogger logger = new BungeeViShopLogger(this.getLogger());

                this.getProxy().getScheduler().schedule(
                        this,
                        new BungeeOrderTask(this.httpClient, config, logger),
                        0L,
                        config.taskInterval,
                        TimeUnit.SECONDS
                );
            } catch (final OkaeriException exception) {
                this.getLogger().severe(exception.getCause().getMessage());
            }
    }

    @Override
    public void onDisable() {
        this.getProxy().getScheduler().cancel(this);
        this.httpClient.dispatcher().executorService().shutdown();
    }

}

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

import java.util.concurrent.TimeUnit;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import okhttp3.OkHttpClient;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.config.EmptyConfigFieldException;
import pl.vishop.plugin.resource.ResourceLoader;
import pl.vishop.plugin.resource.ResourceLoaderException;

public class BungeeViShopPlugin extends Plugin {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();

    @Override
    public void onEnable() {
        final ResourceLoader<Configuration> resourceLoader = new BungeeResourceLoader(this.getClass(), this.getDataFolder());
        try {
            if (resourceLoader.saveDefault("config.yml")) {
                this.getLogger().info("Domyślny plik config.yml zapisany, skonfiguruj go i zrestartuj proxy");
                return;
            }

            final Configuration cfgFile = resourceLoader.load("config.yml");
            final Config config;

            try {
                config = new Config(cfgFile.getString("apiKey"), cfgFile.getString("shopId"), cfgFile.getString("serverId"));
            } catch (final EmptyConfigFieldException exception) {
                this.getLogger().severe(exception.getMessage());
                return;
            }

            this.getProxy().getScheduler().schedule(
                    this,
                    new BungeeOrderTask(this, this.httpClient, config),
                    0L,
                    config.taskInterval.getSeconds(),
                    TimeUnit.SECONDS
            );
        } catch (final ResourceLoaderException exception) {
            this.getLogger().severe(exception.getReason().getMessage("config.yml"));
            this.getLogger().severe("Przyczyna: " + exception.getCause().getMessage());
        }
    }

    @Override
    public void onDisable() {
        this.getProxy().getScheduler().cancel(this);
        this.httpClient.dispatcher().executorService().shutdown();
    }

}

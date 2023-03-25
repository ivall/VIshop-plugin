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

package pl.vishop.plugin.bukkit;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.exception.OkaeriException;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.logger.ViShopLogger;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class BukkitViShopPlugin extends JavaPlugin {

    private final OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .callTimeout(5, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(5, 30, TimeUnit.SECONDS))
            .build();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        try {
            final Config config = ConfigManager.create(Config.class, it -> it
                    .withBindFile(new File(getDataFolder(), "config.yml"))
                    .withConfigurer(new YamlSnakeYamlConfigurer())
                    .saveDefaults()
                    .load(true));
            final ViShopLogger logger = new BukkitViShopLogger(this.getLogger());

            Bukkit.getScheduler().runTaskTimerAsynchronously(
                    this,
                    new BukkitOrderTask(this, this.httpClient, config, logger),
                    0L,
                    config.taskInterval * 20L
            );
        } catch (OkaeriException exception) {
            this.getLogger().severe(exception.getCause().getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        this.httpClient.dispatcher().executorService().shutdown();
    }

}

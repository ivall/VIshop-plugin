/*
 * Copyright (C) 2022 Kamil Trysiński
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

package pl.vishop.vishopplugin;

import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.vishop.vishopplugin.broadcast.BroadcastSender;
import pl.vishop.vishopplugin.broadcast.BukkitBroadcastSender;
import pl.vishop.vishopplugin.broadcast.ModernBroadcastSender;
import pl.vishop.vishopplugin.config.Config;

public final class ViShopPlugin extends JavaPlugin {

    public static final String BACKEND_ADDRESS = "https://dev123.vishop.pl/panel/shops/%1$s/servers/%2$s/payments/%3$s";
    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private BroadcastSender broadcastSender;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        final Config config = new Config();
        if (!config.load(this.getConfig())) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        setupBroadcasts();

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new PendingTask(this, this.httpClient, config), 0L, config.taskInterval);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        this.httpClient.dispatcher().executorService().shutdown();
    }

    private void setupBroadcasts() {
        boolean isSpigot = false;

        try {
            Class.forName("net.md_5.bungee.api.ChatColor");
            isSpigot = true;
        } catch (ClassNotFoundException ignored) {}

        if (isSpigot && Double.parseDouble(Bukkit.getServer().getBukkitVersion().split("-")[0].substring(0, 4)) >= 1.16) {
            this.broadcastSender = new ModernBroadcastSender();
        } else {
            this.broadcastSender = new BukkitBroadcastSender();
        }
    }

    public BroadcastSender getBroadcastSender() {
        return broadcastSender;
    }
}

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

import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.order.OrderTask;

public class BukkitOrderTask extends OrderTask {

    private final Plugin plugin;

    public BukkitOrderTask(final Plugin plugin, final OkHttpClient httpClient, final Config config) {
        super(httpClient, config);
        this.plugin = plugin;
    }

    @Override
    public void logInfo(final String message) {
        this.plugin.getLogger().info(message);
    }

    @Override
    public void logError(final String message) {
        this.plugin.getLogger().severe(message);
    }

    @Override
    public boolean isPlayerOnline(final String playerName) {
        return Bukkit.getPlayerExact(playerName) != null;
    }

    @Override
    public void executeCommand(final String command) {
        Bukkit.getScheduler().runTask(this.plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
    }

}

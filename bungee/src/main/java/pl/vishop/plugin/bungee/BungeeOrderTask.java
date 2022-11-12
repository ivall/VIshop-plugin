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

import java.util.logging.Logger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import okhttp3.OkHttpClient;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.order.OrderTask;

public class BungeeOrderTask extends OrderTask {

    private final ProxyServer proxy;
    private final Logger logger;

    public BungeeOrderTask(final Plugin plugin, final OkHttpClient httpClient, final Config config) {
        super(httpClient, config);
        this.proxy = plugin.getProxy();
        this.logger = plugin.getLogger();
    }

    @Override
    public void logInfo(final String message) {
        this.logger.info(message);
    }

    @Override
    public void logError(final String message) {
        this.logger.severe(message);
    }

    @Override
    public boolean isPlayerOnline(final String playerName) {
        return this.proxy.getPlayer(playerName) != null;
    }

    @Override
    public void executeCommand(final String command) {
        this.proxy.getPluginManager().dispatchCommand(this.proxy.getConsole(), command);
    }

}

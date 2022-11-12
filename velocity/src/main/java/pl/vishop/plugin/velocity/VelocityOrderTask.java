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

import com.velocitypowered.api.proxy.ProxyServer;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.order.OrderTask;

public class VelocityOrderTask extends OrderTask {

    private final ProxyServer proxy;
    private final Logger logger;

    protected VelocityOrderTask(final ProxyServer proxy, final Logger logger, final OkHttpClient httpClient, final Config config) {
        super(httpClient, config);
        this.proxy = proxy;
        this.logger = logger;
    }

    @Override
    public void logInfo(final String message) {
        this.logger.info(message);
    }

    @Override
    public void logError(final String message) {
        this.logger.error(message);
    }

    @Override
    public boolean isPlayerOnline(final String playerName) {
        return this.proxy.getPlayer(playerName).isPresent();
    }

    @Override
    public void executeCommand(final String command) {
        this.proxy.getCommandManager().executeAsync(this.proxy.getConsoleCommandSource(), command);
    }

}

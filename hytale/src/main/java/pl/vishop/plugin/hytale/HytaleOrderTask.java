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

import com.hypixel.hytale.server.core.NameMatching;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.universe.Universe;

import com.hypixel.hytale.server.core.console.ConsoleSender;
import okhttp3.OkHttpClient;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.logger.ViShopLogger;
import pl.vishop.plugin.order.OrderTask;

public class HytaleOrderTask extends OrderTask {

    public HytaleOrderTask(final OkHttpClient httpClient, final Config config, final ViShopLogger logger) {
        super(httpClient, config, logger);
    }

    @Override
    public boolean isPlayerOnline(final String playerName) {
        return Universe.get().getPlayerByUsername(playerName, NameMatching.EXACT) != null;
    }

    @Override
    public void executeCommand(final String command) {
        CommandManager.get().handleCommand(ConsoleSender.INSTANCE, command);
    }

}
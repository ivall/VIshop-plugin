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

import java.util.List;
import java.util.Set;
import okhttp3.OkHttpClient;
import org.bukkit.Bukkit;
import pl.vishop.vishopplugin.config.Config;
import pl.vishop.vishopplugin.order.Order;
import pl.vishop.vishopplugin.request.ConfirmRequester;
import pl.vishop.vishopplugin.request.PendingRequester;

public class PendingTask implements Runnable {

    private final ViShopPlugin plugin;
    private final OkHttpClient httpClient;
    private final Config config;

    public PendingTask(final ViShopPlugin plugin, final OkHttpClient httpClient, final Config config) {
        this.plugin = plugin;
        this.httpClient = httpClient;
        this.config = config;
    }

    @Override
    public void run() {
        final Set<Order> pendingOrders = PendingRequester.get(this.httpClient, this.config);
        if (pendingOrders.isEmpty()) {
            return;
        }

        pendingOrders.stream()
                .filter(order -> !order.requiresOnline() || Bukkit.getPlayerExact(order.getPlayer()) != null)
                .filter(order -> ConfirmRequester.post(this.httpClient, this.config, order))
                .forEach(this::executeCommands);
    }

    private void executeCommands(final Order order) {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            order.getCommands().forEach(command -> {
                if (command.startsWith("#")) {
                    List<String> messages = config.broadcastMessages.get(command.substring(1));
                    if (messages == null) {
                        return;
                    }

                    plugin.getBroadcastSender().broadcastMessages(messages);
                    return;
                }

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            });
        });
    }

}

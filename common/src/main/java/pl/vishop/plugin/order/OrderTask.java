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

package pl.vishop.plugin.order;

import java.util.Arrays;
import okhttp3.OkHttpClient;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.request.ConfirmOrderRequest;
import pl.vishop.plugin.request.PendingOrderRequest;
import pl.vishop.plugin.request.RequestException;

public abstract class OrderTask implements Runnable {

    private final PendingOrderRequest pendingOrderRequest;
    private final ConfirmOrderRequest confirmOrderRequest;

    protected OrderTask(final OkHttpClient httpClient, final Config config) {
        this.pendingOrderRequest = new PendingOrderRequest(httpClient, config);
        this.confirmOrderRequest = new ConfirmOrderRequest(httpClient, config);
    }

    public abstract void logInfo(final String message);

    public abstract void logError(final String message);

    public abstract boolean isPlayerOnline(final String playerName);

    public abstract void executeCommand(final String command);

    @Override
    public void run() {
        try {
            Arrays.stream(this.pendingOrderRequest.get()).forEach(this::processOrder);
        } catch (final RequestException exception) {
            this.logError("Nieudane pobranie zamówień z ViShop");
            this.logError("Przyczyna: " + exception.getMessage());
        }
    }

    private void processOrder(final Order order) {
        if (order.requiresOnline() && !this.isPlayerOnline(order.getPlayer())) {
            return;
        }

        try {
            this.confirmOrderRequest.put(order);
            order.getCommands().forEach(command -> {
                this.logInfo(String.format("Wykonywanie komendy dla zamówienia %s: %s", order.getId(), command));
                this.executeCommand(command);
            });
        } catch (final RequestException exception) {
            this.logError(String.format("Nieudane potwierdzenie zamówienia %s w ViShop", order.getId()));
            this.logError("Przyczyna: " + exception.getMessage());
        }
    }

}

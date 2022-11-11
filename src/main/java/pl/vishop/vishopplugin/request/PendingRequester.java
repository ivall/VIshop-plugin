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

package pl.vishop.vishopplugin.request;

import com.google.gson.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.bukkit.Bukkit;
import pl.vishop.vishopplugin.ViShopPlugin;
import pl.vishop.vishopplugin.config.Config;
import pl.vishop.vishopplugin.order.Order;

import java.io.IOException;
import java.util.*;

public final class PendingRequester {

    private PendingRequester() {}

    public static Set<Order> get(final OkHttpClient httpClient, final Config config) {
        final Set<Order> pendingOrders = new HashSet<>();
        final Request request = new Builder()
                .url(getUrl(config))
                .header("User-Agent", "ViShopPlugin/1.0")
                .header("Authorization", config.apiKey)
                .build();

        try (final Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Otrzymany kod odpowiedzi " + response.code());
            }

            final ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Puste body odpowiedzi");
            }

            final JsonArray orders = JsonParser.parseString(responseBody.string()).getAsJsonArray();

            for (Object value : orders)
            {
                final JsonObject order = (JsonObject) value;
                final JsonObject product = (JsonObject) order.get("product");

                final UUID orderId = UUID.fromString(order.get("id").getAsString());
                final String player = order.get("player").getAsString();
                final boolean requireOnline = product.get("require_player_online").getAsBoolean();
                final List<String> commands = new ArrayList<>();

                final JsonArray commandsArray = (JsonArray) product.get("commands");
                for (JsonElement o : commandsArray)
                {
                    commands.add(o.getAsString());
                }

                pendingOrders.add(new Order(orderId, player, requireOnline, commands));
            }
        } catch (final IOException | JsonIOException exception) {
            Bukkit.getLogger().warning("Nieudane pobranie oczekujących zamówień z ViShop:");
            Bukkit.getLogger().warning(exception.getMessage());
        }

        return pendingOrders;
    }

    private static String getUrl(final Config config) {
        return String.format(ViShopPlugin.BACKEND_ADDRESS, config.shopId, config.serverId, "?status=executing");
    }

}

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.bukkit.Bukkit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.vishop.vishopplugin.config.Config;
import pl.vishop.vishopplugin.order.Order;

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

            final JSONArray orders = new JSONArray(responseBody.string());
            for (int i = 0; i < orders.length(); i++) {
                final JSONObject order = orders.getJSONObject(i);
                final JSONObject product = order.getJSONObject("product");

                final UUID orderId = UUID.fromString(order.getString("id"));
                final String player = order.getString("player");
                final boolean requireOnline = product.getBoolean("require_player_online");
                final List<String> commands = new ArrayList<>();

                final JSONArray commandsArray = product.getJSONArray("commands");
                for (int j = 0; j < commandsArray.length(); j++) {
                    commands.add(commandsArray.getString(j));
                }

                pendingOrders.add(new Order(orderId, player, requireOnline, commands));
            }
        } catch (final IOException | JSONException exception) {
            Bukkit.getLogger().warning("Nieudane pobranie oczekujących zamówień z ViShop:");
            Bukkit.getLogger().warning(exception.getMessage());
        }

        return pendingOrders;
    }

    private static String getUrl(final Config config) {
        return "https://vishop.pl/panel/shops/" + config.shopId + "/servers/" + config.serverId + "/payments/?status=executing";
    }

}

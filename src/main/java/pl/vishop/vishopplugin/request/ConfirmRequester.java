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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.bukkit.Bukkit;
import org.json.JSONException;
import pl.vishop.vishopplugin.ViShopPlugin;
import pl.vishop.vishopplugin.config.Config;
import pl.vishop.vishopplugin.order.Order;

public final class ConfirmRequester {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private ConfirmRequester() {}

    public static boolean post(final OkHttpClient httpClient, final Config config, final Order order) {
        final Request request = new Builder()
                .url(getUrl(config, order))
                .header("User-Agent", "ViShopPlugin/1.0")
                .header("Authorization", config.apiKey)
                .put(RequestBody.create("", JSON_MEDIA_TYPE))
                .build();

        try (final Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Otrzymany kod odpowiedzi " + response.code());
            }

            return true;
        } catch (final IOException | JSONException exception) {
            Bukkit.getLogger().warning("Nieudane potwierdzenie zamówienia " + order.getOrderId().toString() + " w ViShop:");
            Bukkit.getLogger().warning(exception.getMessage());
        }

        return false;
    }

    private static String getUrl(final Config config, final Order order) {
        return String.format(ViShopPlugin.BACKEND_ADDRESS, config.shopId, config.serverId, order.getOrderId().toString() + "/");
    }

}

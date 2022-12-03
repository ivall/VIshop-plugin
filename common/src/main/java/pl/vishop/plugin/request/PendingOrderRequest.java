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

package pl.vishop.plugin.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.logger.ViShopLogger;
import pl.vishop.plugin.order.Order;

public final class PendingOrderRequest extends ViShopRequest {

    private final Gson gson;
    private final OkHttpClient httpClient;
    private final Config config;
    private final ViShopLogger logger;

    public PendingOrderRequest(final OkHttpClient httpClient, final Config config, final ViShopLogger logger) {
        this.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        this.httpClient = httpClient;
        this.config = config;
        this.logger = logger;
    }

    public Order[] get() throws RequestException {
        final Request request = this.prepareGetRequest(this.getRequestUrl(), this.config.apiKey);

        if (this.config.debug) {
            this.logger.debug(String.format("Sending GET request to url: %s", request.url()));
            this.logger.debug(String.format("Attaching API key: %s", this.config.apiKey));
        }

        try (final Response response = this.httpClient.newCall(request).execute()) {
            final String responseBody = response.body() != null ? response.body().string() : null;
            if (this.config.debug) {
                this.logger.debug(String.format("Response for GET request: %s", responseBody));
            }

            if (!response.isSuccessful()) {
                throw new RequestException("Otrzymany kod odpowiedzi " + response.code());
            }

            if (responseBody == null) {
                throw new RequestException("Puste body odpowiedzi");
            }

            try {
                return this.gson.fromJson(responseBody, Order[].class);
            } catch (final JsonSyntaxException exception) {
                throw new RequestException(exception.getMessage());
            }
        } catch (final IOException exception) {
            throw new RequestException(exception.getMessage());
        }
    }

    private HttpUrl getRequestUrl() {
        final String urlString = String.format(BACKEND_ADDRESS, this.config.shopId, this.config.serverId, "?status=executing");
        return HttpUrl.parse(urlString);
    }

}

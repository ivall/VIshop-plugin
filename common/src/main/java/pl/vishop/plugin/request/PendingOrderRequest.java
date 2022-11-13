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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.order.Order;

public final class PendingOrderRequest extends ViShopRequest {

    private final Gson gson;
    private final OkHttpClient httpClient;
    private final Config config;
    private final String requestUrl;

    public PendingOrderRequest(final OkHttpClient httpClient, final Config config) {
        this.gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        this.httpClient = httpClient;
        this.config = config;
        this.requestUrl = String.format(BACKEND_ADDRESS, config.shopId, config.serverId, "?status=executing");
    }

    public Order[] get() throws RequestException {
        final Request request = this.prepareGetRequest(this.requestUrl, this.config.apiKey);

        try (final Response response = this.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RequestException("Otrzymany kod odpowiedzi " + response.code());
            }

            final ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new RequestException("Puste body odpowiedzi");
            }

            try {
                return this.gson.fromJson(responseBody.string(), Order[].class);
            } catch (final JsonSyntaxException exception) {
                throw new RequestException(exception.getMessage());
            }
        } catch (final IOException exception) {
            throw new RequestException(exception.getMessage());
        }
    }

}

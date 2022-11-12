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

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.vishop.plugin.config.Config;
import pl.vishop.plugin.order.Order;

public final class ConfirmOrderRequest extends ViShopRequest {

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final RequestBody REQUEST_BODY = RequestBody.create("", JSON_MEDIA_TYPE);

    private final OkHttpClient httpClient;
    private final Config config;

    public ConfirmOrderRequest(final OkHttpClient httpClient, final Config config) {
        this.httpClient = httpClient;
        this.config = config;
    }

    public void put(final Order order) throws RequestException {
        final Request request = this.preparePutRequest(this.getRequestUrl(order), this.config.apiKey, REQUEST_BODY);

        try (final Response response = this.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RequestException("Otrzymany kod odpowiedzi " + response.code());
            }
        } catch (final IOException exception) {
            throw new RequestException(exception.getMessage());
        }
    }

    private String getRequestUrl(final Order order) {
        return String.format(BACKEND_ADDRESS, this.config.shopId, this.config.serverId, order.getId() + "/");
    }

}

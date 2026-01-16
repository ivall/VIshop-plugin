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

import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ViShopRequest {

    public static final String BACKEND_ADDRESS = "https://dev123.vishop.pl/panel/shops/%1$s/servers/%2$s/payments/%3$s";

    protected Request prepareGetRequest(final HttpUrl url, final String apiKey) {
        return this.prepareRequestBuilder(url, apiKey).build();
    }

    protected Request preparePutRequest(final HttpUrl url, final String apiKey, final RequestBody body) {
        return this.prepareRequestBuilder(url, apiKey).put(body).build();
    }

    private Request.Builder prepareRequestBuilder(final HttpUrl url, final String apiKey) {
        return new Request.Builder()
                .url(url)
                .header("User-Agent", "ViShopPlugin/2.5")
                .header("Authorization", apiKey);
    }

}

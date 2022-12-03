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

package pl.vishop.plugin.config;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class Config {

    public final String apiKey;
    public final String shopId;
    public final String serverId;
    public final boolean debug;

    public final Duration taskInterval = Duration.of(30, ChronoUnit.SECONDS);

    public Config(final ConfigLoader loader) throws EmptyConfigFieldException {
        this.apiKey = loader.getString("apiKey");
        this.shopId = loader.getString("shopId");
        this.serverId = loader.getString("serverId");
        this.debug = loader.getBoolean("debug");
        this.checkValues();
    }

    private void checkValues() throws EmptyConfigFieldException {
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new EmptyConfigFieldException("klucz API");
        }

        if (this.shopId == null || this.shopId.isEmpty()) {
            throw new EmptyConfigFieldException("ID sklepu");
        }

        if (this.serverId == null || this.serverId.isEmpty()) {
            throw new EmptyConfigFieldException("ID serwera");
        }
    }

}

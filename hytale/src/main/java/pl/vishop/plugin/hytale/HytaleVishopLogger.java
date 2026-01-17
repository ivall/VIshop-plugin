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

package pl.vishop.plugin.hytale;

import com.hypixel.hytale.logger.HytaleLogger;
import pl.vishop.plugin.logger.ViShopLogger;

import java.util.logging.Level;

public class HytaleVishopLogger implements ViShopLogger {

    private final HytaleLogger logger;

    public HytaleVishopLogger(final HytaleLogger logger) {
        this.logger = logger;
    }

    @Override
    public void info(final String message) {
        this.logger.at(Level.INFO).log(message);
    }

    @Override
    public void error(final String message) {
        this.logger.at(Level.SEVERE).log(message);
    }

    @Override
    public void debug(final String message) {
        this.info(String.format("[DEBUG] %s", message));
    }

}
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

import pl.vishop.plugin.config.ConfigLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class HytaleConfigLoader implements ConfigLoader {

    private final Properties properties;

    public HytaleConfigLoader(final Path configDirectory) throws IOException {
        this.properties = new Properties();

        Files.createDirectories(configDirectory);
        Path configFile = configDirectory.resolve("config.properties");

        if (!Files.exists(configFile)) {
            this.createDefaultConfig(configFile);
        }

        try (InputStream input = Files.newInputStream(configFile)) {
            this.properties.load(input);
        }
    }

    private void createDefaultConfig(final Path configFile) throws IOException {
        try (OutputStream output = Files.newOutputStream(configFile)) {
            Properties defaults = new Properties();
            defaults.setProperty("apiKey", "");
            defaults.setProperty("shopId", "");
            defaults.setProperty("serverId", "");
            defaults.setProperty("debug", "false");

            defaults.store(output, "VIshop");
        }
    }

    @Override
    public boolean getBoolean(final String key) {
        return Boolean.parseBoolean(this.properties.getProperty(key, "false"));
    }

    @Override
    public String getString(final String key) {
        return this.properties.getProperty(key, null);
    }

}
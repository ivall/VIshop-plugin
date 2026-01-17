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

import org.yaml.snakeyaml.Yaml;
import pl.vishop.plugin.config.ConfigLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class HytaleConfigLoader implements ConfigLoader {

    private final Map<String, Object> config;

    public HytaleConfigLoader(final Path configDirectory) throws IOException {
        Files.createDirectories(configDirectory);

        Path configFile = configDirectory.resolve("config.yml");

        if (!Files.exists(configFile)) {
            copyDefaultConfig(configFile);
        }

        try (InputStream input = Files.newInputStream(configFile)) {
            Yaml yaml = new Yaml();
            this.config = yaml.load(input);
        }
    }

    private void copyDefaultConfig(final Path target) throws IOException {
        try (InputStream input = HytaleConfigLoader.class.getClassLoader().getResourceAsStream("config.yml")) {

            if (input == null) {
                throw new IOException("Brak config.yml w jar");
            }

            Files.copy(input, target);
        }
    }

    @Override
    public boolean getBoolean(final String key) {
        Object value = this.config.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return false;
    }

    @Override
    public String getString(final String key) {
        Object value = this.config.get(key);
        return value != null ? value.toString() : null;
    }
}
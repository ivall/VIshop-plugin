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

package pl.vishop.plugin.bukkit;

import org.bukkit.configuration.ConfigurationSection;
import pl.vishop.plugin.config.ConfigLoader;

public class BukkitConfigLoader implements ConfigLoader {

    private final ConfigurationSection configFile;

    public BukkitConfigLoader(final ConfigurationSection configFile) {
        this.configFile = configFile;
    }

    @Override
    public boolean getBoolean(final String key) {
        return this.configFile.getBoolean(key);
    }

    @Override
    public String getString(final String key) {
        return this.configFile.getString(key);
    }

}

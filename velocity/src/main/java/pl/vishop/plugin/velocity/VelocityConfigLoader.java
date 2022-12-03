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

package pl.vishop.plugin.velocity;

import ninja.leaping.configurate.ConfigurationNode;
import pl.vishop.plugin.config.ConfigLoader;

public class VelocityConfigLoader implements ConfigLoader {

    private final ConfigurationNode configFile;

    public VelocityConfigLoader(final ConfigurationNode configFile) {
        this.configFile = configFile;
    }

    @Override
    public boolean getBoolean(final String key) {
        return this.configFile.getNode(key).getBoolean();
    }

    @Override
    public String getString(final String key) {
        return this.configFile.getNode(key).getString();
    }

}

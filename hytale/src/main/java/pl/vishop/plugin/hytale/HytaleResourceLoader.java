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

import java.nio.file.Path;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import pl.vishop.plugin.resource.ResourceLoader;
import pl.vishop.plugin.resource.ResourceLoaderException;
import pl.vishop.plugin.resource.ResourceLoaderException.Reason;

public class HytaleResourceLoader extends ResourceLoader<ConfigurationNode> {

    public HytaleResourceLoader(final Class<?> loadingClass, final Path dataDirectory) {
        super(loadingClass, dataDirectory);
    }

    @Override
    protected ConfigurationNode loadResource(final Path resourcePath) throws ResourceLoaderException {
        try {
            return YamlConfigurationLoader.builder().path(resourcePath).build().load();
        } catch (final Exception exception) {
            throw new ResourceLoaderException(Reason.FILE_NOT_LOADED, exception);
        }
    }

}
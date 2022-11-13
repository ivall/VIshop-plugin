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

package pl.vishop.plugin.resource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import pl.vishop.plugin.resource.ResourceLoaderException.Reason;

public abstract class ResourceLoader<T> {

    private final Class<?> loadingClass;
    private final Path dataDirectory;

    protected ResourceLoader(final Class<?> loadingClass, final Path dataDirectory) {
        this.loadingClass = loadingClass;
        this.dataDirectory = dataDirectory;
    }

    protected abstract T loadResource(final Path resourcePath) throws ResourceLoaderException;

    public boolean saveDefault(final String resourceName) throws ResourceLoaderException {
        if (!Files.exists(this.dataDirectory)) {
            try {
                Files.createDirectories(this.dataDirectory);
            } catch (final Exception exception) {
                throw new ResourceLoaderException(Reason.DIRECTORY_NOT_CREATED, exception);
            }
        }

        final Path resourcePath = this.dataDirectory.resolve(resourceName);
        if (!Files.exists(resourcePath)) {
            try (final InputStream in = this.loadingClass.getClassLoader().getResourceAsStream(resourceName)) {
                Files.copy(Objects.requireNonNull(in), resourcePath);
                return true;
            } catch (final Exception exception) {
                throw new ResourceLoaderException(Reason.DEFAULT_FILE_NOT_SAVED, exception);
            }
        }

        return false;
    }

    public T load(final String resourceName) throws ResourceLoaderException {
        return this.loadResource(this.dataDirectory.resolve(resourceName));
    }

}

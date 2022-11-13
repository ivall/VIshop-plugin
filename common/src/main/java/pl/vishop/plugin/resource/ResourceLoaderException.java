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

public class ResourceLoaderException extends Exception {

    private final Reason reason;

    public ResourceLoaderException(final Reason reason, final Throwable cause) {
        super(cause);
        this.reason = reason;
    }

    public Reason getReason() {
        return this.reason;
    }

    public enum Reason {
        DIRECTORY_NOT_CREATED("Nie udało się utworzyć folderu pluginu"),
        DEFAULT_FILE_NOT_SAVED("Nie udało się zapisać domyślnego pliku %s"),
        FILE_NOT_LOADED("Nie udało się otworzyć pliku %s");

        private final String message;

        Reason(final String message) {
            this.message = message;
        }

        public String getMessage(final String fileName) {
            return String.format(this.message, fileName);
        }
    }

}

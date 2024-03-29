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

package pl.vishop.plugin.order;

import java.util.List;

public class Product {

    private final boolean requirePlayerOnline;
    private final List<String> commands;

    public Product(final boolean requirePlayerOnline, final List<String> commands) {
        this.requirePlayerOnline = requirePlayerOnline;
        this.commands = commands;
    }

    public boolean requiresPlayerOnline() {
        return this.requirePlayerOnline;
    }

    public List<String> getCommands() {
        return this.commands;
    }

}

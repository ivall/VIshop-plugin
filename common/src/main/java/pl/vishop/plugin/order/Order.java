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
import java.util.UUID;
import java.util.stream.Collectors;

public class Order {

    private final UUID id;
    private final String player;
    private final Product product;

    public Order(final UUID id, final String player, final Product product) {
        this.id = id;
        this.player = player;
        this.product = product;
    }

    public UUID getId() {
        return this.id;
    }

    public String getPlayer() {
        return this.player;
    }

    public boolean requiresOnline() {
        return this.product.requiresPlayerOnline();
    }

    public List<String> getCommands() {
        return this.product.getCommands().stream()
                .map(command -> command.replace("{NICK}", this.player))
                .collect(Collectors.toList());
    }

}

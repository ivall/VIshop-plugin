/*
 * Copyright (C) 2022 Kamil Trysiński
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

package pl.vishop.vishopplugin.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

public class Config {

    public String apiKey;
    public String shopId;
    public String serverId;

    public long taskInterval;

    public boolean load(final ConfigurationSection cfg) {
        this.apiKey = cfg.getString("apiKey", "");
        if (this.apiKey.isEmpty()) {
            Bukkit.getLogger().warning(ChatColor.RED + "Musisz uzupełnić klucz API w pliku konfiguracyjnym, aby plugin działał. Wyłączam...");
            return false;
        }

        this.shopId = cfg.getString("shopId", "");
        if (this.shopId.isEmpty()) {
            Bukkit.getLogger().warning(ChatColor.RED + "Musisz uzupełnić klucz API w pliku konfiguracyjnym, aby plugin działał. Wyłączam...");
            return false;
        }

        this.serverId = cfg.getString("serverId", "");
        if (this.serverId.isEmpty()) {
            Bukkit.getLogger().warning(ChatColor.RED + "Musisz uzupełnić klucz API w pliku konfiguracyjnym, aby plugin działał. Wyłączam...");
            return false;
        }

        this.taskInterval = 600L;
        return true;
    }

}

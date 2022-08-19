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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

    public String apiKey;
    public String shopId;
    public String serverId;
    public Map<String, List<String>> broadcastMessages;

    public long taskInterval;

    public boolean load(final ConfigurationSection cfg) {
        this.apiKey = cfg.getString("apiKey", "");
        if (this.apiKey.isEmpty()) {
            Bukkit.getLogger().warning("Uzupełnij klucz API w configu, aby łączyć się ze swoim sklepem w ViShop");
            return false;
        }

        this.shopId = cfg.getString("shopId", "");
        if (this.shopId.isEmpty()) {
            Bukkit.getLogger().warning("Uzupełnij ID sklepu w configu, aby łączyć się ze swoim sklepem w ViShop");
            return false;
        }

        this.serverId = cfg.getString("serverId", "");
        if (this.serverId.isEmpty()) {
            Bukkit.getLogger().warning("Uzupełnij ID serwera w configu, aby łączyć się ze swoim sklepem w ViShop");
            return false;
        }

        this.broadcastMessages = new HashMap<>();
        ConfigurationSection broadcastSection = cfg.getConfigurationSection("broadcasts");
        boolean isSpigot = false;
        try {
            Class.forName("net.md_5.bungee.api.ChatColor");
            isSpigot = true;
        } catch (ClassNotFoundException ignored) {}

        boolean translateHex = isSpigot && (Bukkit.getServer().getVersion().contains("1.16") || Bukkit.getServer().getVersion().contains("1.17") || Bukkit.getServer().getVersion().contains("1.18") || Bukkit.getServer().getVersion().contains("1.19"));
        broadcastSection.getKeys(false).forEach(id -> {
            List<String> messages = broadcastSection.getStringList(id);
            int i = 0;
            for (String msg : messages) {
                msg = ChatColor.translateAlternateColorCodes('&', msg);
                if (translateHex) msg = translateHex(msg);
                messages.set(i, msg);
                i++;
            }

            this.broadcastMessages.put(id, messages);
        });

        this.taskInterval = 600L;
        return true;
    }

    private static final Pattern hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");;
    private String translateHex(String message) {
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            String hex = message.substring(matcher.start(), matcher.end());
            message = message.replace(hex, net.md_5.bungee.api.ChatColor.of(hex
                    .replace("&", "")) + "");
            matcher = hexPattern.matcher(message);
        }
        return message;
    }

}

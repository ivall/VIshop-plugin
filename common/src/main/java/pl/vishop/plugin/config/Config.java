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

package pl.vishop.plugin.config;


import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;

public class Config extends OkaeriConfig {
    @Comment("Klucz API do autoryzacji połączeń")
    @Comment("Znajdziesz go w panelu sklepu, w zakładce Ustawienia")
    public String apiKey = "";
    @Comment("ID sklepu")
    @Comment("Znajdziesz je na głównej stronie panelu sklepu")
    public String shopId = "";
    @Comment("ID serwera w sklepie")
    @Comment("Znajdziesz je w panelu sklepu, w zakładce Serwery")
    public String serverId = "";
    @Comment("Czy włączyć logi debug?")
    @Comment("")
    @Comment("UWAGA — logi debug powinny być włączone tylko w przypadku problemów z pluginem")
    @Comment("Po ich ustąpieniu — powinny znowu zostać wyłączone, a stare pliki logów skasowane")
    @Comment("Uruchomienie tych logów będzie pokazywać w konsoli wrażliwe informacje:")
    @Comment(" - klucz API")
    @Comment(" - adresy, z którymi łączy się plugin")
    @Comment(" - dane wysyłane przez ViShop")
    public boolean debug = false;

    public long taskInterval = 30;

    public Config() throws EmptyConfigFieldException {
        this.checkValues();
    }

    private void checkValues() throws EmptyConfigFieldException {
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new EmptyConfigFieldException("klucz API");
        }

        if (this.shopId == null || this.shopId.isEmpty()) {
            throw new EmptyConfigFieldException("ID sklepu");
        }

        if (this.serverId == null || this.serverId.isEmpty()) {
            throw new EmptyConfigFieldException("ID serwera");
        }
    }



}

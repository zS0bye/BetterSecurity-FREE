/*
 * Security plugin for your server - https://github.com/KyotoResources/BetterSecurity
 * Copyright (C) 2023 KyotoResources
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.zs0bye.bettersecurity.bungee.files.readers;

import it.zs0bye.bettersecurity.bungee.files.FileHandler;
import it.zs0bye.bettersecurity.bungee.files.ConfigReader;
import it.zs0bye.bettersecurity.bungee.files.FileType;
import it.zs0bye.bettersecurity.bungee.modules.Module;
import it.zs0bye.bettersecurity.bungee.utils.StringUtils;
import it.zs0bye.bettersecurity.common.utils.CStringUtils;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.config.Configuration;

import java.util.*;

public enum Command implements ConfigReader {
    INSTANCE(""),
    WARNING("warning"),
    METHOD("method"),
    FORCE_CHECK("force_check"),
    EXECUTORS("executors"),
    BLOCKS_COMMANDS("commands"),
    SERVER_MODE_ENABLED("server_mode.enabled"),
    SERVER_MODE_SERVERS("server_mode.servers"),
    SERVER_MODE_METHOD(".method"),
    SERVER_MODE_EXECUTORS(".executors"),
    SERVER_MODE_COMMANDS(".commands");

    @Getter
    private final String path;
    @Getter
    private final FileType type;
    private Configuration config;

    Command(final String path) {
        this.path = path;
        this.type = Module.COMMANDS.getType();
        this.reloadConfig();
    }

    @Override
    public String variables(final String... var) {
        StringBuilder builder = new StringBuilder();
        for(String texts : var) builder.append(texts);
        builder.append(path);
        return builder.toString();
    }

    @Override
    public void reloadConfig() {
        final Configuration config = FileHandler.getConfig(this.type);
        if(config == null) return;
        this.config = config;
    }

    @Override
    public String getString(final String... var) {
        return StringUtils.colorize(this.config.getString(this.variables(var)));
    }

    @Override
    public List<String> getStringList(final String... var) {
        List<String> list = new ArrayList<>();
        for (String setList : this.config.getStringList(this.variables(var))) {
            list.add(StringUtils.colorize(setList));
        }
        return list;
    }

    @Override
    public boolean getBoolean(final String... var) {
        return this.config.getBoolean(this.variables(var));
    }

    @Override
    public boolean contains(final String... var) {
        return this.config.contains(this.variables(var));
    }

    @Override
    public int getInt(final String... var) {
        return this.config.getInt(this.variables(var));
    }

    @Override
    public String getCustomString(String replace, final String... var) {
        if (replace.startsWith("%prefix%")) {
            replace = replace.replace("%prefix%", Config.SETTINGS_PREFIX.getString());
            if (replace.startsWith(Config.SETTINGS_PREFIX.getString() + "%center%")) {
                replace = replace.replace("%center%", "");
                return CStringUtils.center(replace);
            }
            return replace;
        }

        if(replace.startsWith("%center%")) {
            replace = replace.replace("%center%", "");
            return CStringUtils.center(replace);
        }
        return replace;
    }

    @Override
    public String getCustomString(final String... var) {
        return this.getCustomString(this.getString(var), var);
    }

    @Override
    public void send(final CommandSender sender, final String... var) {
        this.send(sender, new HashMap<>(), var);
    }

    @Override
    public void send(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        String message = this.getCustomString(this.getString(var), var);
        if (message.isEmpty()) return;
        for (String key : placeholders.keySet()) message = message.replace(key, placeholders.get(key));
        StringUtils.send(sender, message);
    }

    @Override
    public void sendList(final CommandSender sender, final String... var) {
        this.sendList(sender, new HashMap<>(), var);
    }

    @Override
    public void sendList(final CommandSender sender, final Map<String, String> placeholders, final String... var) {
        if (this.getStringList(var).isEmpty()) return;
        this.getStringList(var).forEach(msg -> {
            for (String key : placeholders.keySet()) msg = msg.replace(key, placeholders.get(key));
            sender.sendMessage(TextComponent.fromLegacyText(this.getCustomString(msg, var)));
        });
    }

    @Override
    public Collection<String> getSection(final String... var) {
        return this.config.getSection(this.variables(var)).getKeys();
    }

}

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

package it.zs0bye.bettersecurity.bungee.listeners;

import it.zs0bye.bettersecurity.bungee.BetterSecurityBungee;
import it.zs0bye.bettersecurity.bungee.BungeeUser;
import it.zs0bye.bettersecurity.bungee.files.readers.Tab;
import it.zs0bye.bettersecurity.bungee.modules.Module;
import it.zs0bye.bettersecurity.common.BetterUser;
import it.zs0bye.bettersecurity.common.SoftwareType;
import it.zs0bye.bettersecurity.common.modules.tabcomplete.TabHandler;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManageTabCompleteListener implements Listener {

    private final BetterSecurityBungee plugin;

    public ManageTabCompleteListener(final BetterSecurityBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTabComplete(final TabCompleteEvent event) {
        if(Module.TAB_COMPLETE.isDisabled()) return;
        final BetterUser user = new BungeeUser((ProxiedPlayer) event.getSender());
        final String completion = event.getCursor();
        final List<String> suggestions = event.getSuggestions();
        final TabHandler handler = new TabHandler(this.plugin.getLogger(), user, Tab.class, SoftwareType.PROXY);
        handler.injectTabSuggestions(suggestions, completion, event::setCancelled);
        handler.injectTabChildrens(completion, suggestions, event::setCancelled);
    }

}

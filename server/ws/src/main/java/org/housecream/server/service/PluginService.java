/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import javax.annotation.PostConstruct;
import org.housecream.plugins.api.HousecreamPlugin;
import org.housecream.server.api.domain.Plugin;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.google.common.base.Preconditions;

@Service
public class PluginService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ServiceLoader<HousecreamPlugin> pluginLoader = ServiceLoader.load(HousecreamPlugin.class);

    @PostConstruct
    protected void postConstruct() {
        // Lazy load of SPI implementation cause multiple thread calling getPlugins() for the first
        // time to return a not full list for one of the thread. We need to preload plugins 
        List<HousecreamPlugin> plugins = getPlugins();
        for (HousecreamPlugin housecreamPlugin : plugins) {
            log.info("Housecream plugin found : " + housecreamPlugin);
        }
    }

    public List<Plugin> getDescriptions() {
        List<Plugin> descriptions = new ArrayList<>();
        for (HousecreamPlugin housecreamPlugin : getPlugins()) {
            descriptions.add(housecreamPlugin.plugin());
        }
        return descriptions;
    }

    private List<HousecreamPlugin> getPlugins() {
        Iterator<HousecreamPlugin> pluginIt = pluginLoader.iterator();
        List<HousecreamPlugin> result = new ArrayList<>();
        while (pluginIt.hasNext()) {
            HousecreamPlugin plugin = pluginIt.next();
            result.add(plugin);
        }
        return result;
    }

    public URI validateAndNormalizeURI(URI uri) throws PluginNotFoundException {
        Preconditions.checkNotNull(uri, "uri cannot be null");
        HousecreamPlugin pluginFromScheme = getPlugin(uri.getScheme());
        URI normalizedUri = pluginFromScheme.validateAndNormalizeUri(uri);
        if (normalizedUri == null) {
            return uri;
        }
        return normalizedUri;
    }

    public HousecreamPlugin getPlugin(String id) throws PluginNotFoundException {
        Preconditions.checkNotNull(id, "id cannot be null");
        Iterator<HousecreamPlugin> pluginIt = pluginLoader.iterator();
        while (pluginIt.hasNext()) {
            HousecreamPlugin plugin = pluginIt.next();
            if (id.equals(plugin.plugin().getId())) {
                return plugin;
            }
        }
        throw new PluginNotFoundException("Cannot found plugin for id : " + id);
    }
}

package net.awired.housecream.server.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import javax.annotation.PostConstruct;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
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

    public List<HousecreamPlugin> getPlugins() {
        Iterator<HousecreamPlugin> pluginIt = pluginLoader.iterator();
        List<HousecreamPlugin> result = new ArrayList<HousecreamPlugin>();
        while (pluginIt.hasNext()) {
            HousecreamPlugin plugin = pluginIt.next();
            result.add(plugin);
        }
        return result;
    }

    public URI validateAndNormalizeURI(URI uri) throws PluginNotFoundException {
        Preconditions.checkNotNull(uri, "uri cannot be null");
        HousecreamPlugin pluginFromScheme = getPluginFromScheme(uri.getScheme());
        return pluginFromScheme.validateAndNormalizeUri(uri);
    }

    public HousecreamPlugin getPluginFromScheme(String prefix) throws PluginNotFoundException {
        Preconditions.checkNotNull(prefix, "prefix cannot be null");
        Iterator<HousecreamPlugin> pluginIt = pluginLoader.iterator();
        while (pluginIt.hasNext()) {
            HousecreamPlugin plugin = pluginIt.next();
            if (prefix.equals(plugin.scheme())) {
                return plugin;
            }
        }
        throw new PluginNotFoundException("Cannot found plugin for prefix : " + prefix);
    }
}

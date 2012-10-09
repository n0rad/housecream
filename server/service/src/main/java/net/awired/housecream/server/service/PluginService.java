package net.awired.housecream.server.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import javax.annotation.PostConstruct;
import net.awired.housecream.plugins.api.HousecreamPlugin;
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

    public HousecreamPlugin getPluginFromPrefix(String prefix) {
        Preconditions.checkNotNull(prefix, "prefix cannot be null");
        Iterator<HousecreamPlugin> pluginIt = pluginLoader.iterator();
        while (pluginIt.hasNext()) {
            HousecreamPlugin plugin = pluginIt.next();
            if (prefix.equals(plugin.prefix())) {
                return plugin;
            }
        }
        throw new IllegalStateException("Cannot found plugin for prefix : " + prefix);
    }
}

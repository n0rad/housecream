package org.housecream.server.resource;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.tika.Tika;
import org.housecream.server.api.domain.Plugin;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.resource.ChannelsResource.PluginResource;
import org.housecream.server.api.resource.ChannelsResource.PluginsResource;
import org.housecream.server.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by n0rad on 14/04/14.
 */
@Component
public class PluginsResourceImpl implements PluginsResource {

    @Autowired
    private PluginResource pluginResource;

    @Autowired
    private PluginService pluginService;

    @Override
    public List<Plugin> descriptions() {
        return pluginService.getDescriptions();
    }

    @Override
    public PluginResource plugin(String id) {
        return pluginResource;
    }

    @Component
    public static class PluginResourceImpl implements PluginResource {

        private static final URL DEFAULT_LOGO = PluginResourceImpl.class.getResource("/plugin-default-logo.png");

        @Autowired
        private PluginService pluginService;

        private Tika tika = new Tika();

        @Override
        public void activate(String id) {

        }

        @Override
        public Response getLogo(String id) throws IOException, PluginNotFoundException {
            URL url = pluginService.getPlugin(id).getLogo();
            if (url == null) {
                url = DEFAULT_LOGO;
            }
            return Response.ok(url.openStream()).type(tika.detect(url)).build();
        }

        @Override
        public Plugin getPlugin(String id) throws PluginNotFoundException {
            return pluginService.getPlugin(id).plugin();
        }

    }

}

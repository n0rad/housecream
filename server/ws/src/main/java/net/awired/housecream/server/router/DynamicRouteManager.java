package net.awired.housecream.server.router;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.service.PluginService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DynamicRouteManager extends RouteBuilder {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private ModelCamelContext camelContext;

    @Inject
    private InEventTransformer eventTransformer;

    @Inject
    private PluginService pluginService;

    private Map<URI, RouteDefinition> pointRoutes = Collections.synchronizedMap(new HashMap<URI, RouteDefinition>());

    public void registerInRoute(InPoint point) {
        try {
            URI uri = point.getUri();
            HousecreamPlugin plugin = pluginService.getPluginFromScheme(uri.getScheme());
            RouteDefinition routeDefinition;
            if (plugin.isCommand()) {
                routeDefinition = from(point.getUri().toString()).to(StaticRouteManager.DIRECT_COMMAND);
            } else {
                routeDefinition = from(point.getUri().toString()) //
                        .setHeader(InEventTransformer.PLUGIN_HEADER_NAME, constant(plugin)) //
                        .setHeader(InEventTransformer.INPOINT_HEADER_NAME, constant(point)) //
                        .process(eventTransformer) //
                        .to("seda:" + point.getId()) //
                        .to(StaticRouteManager.DIRECT_ENGINE); // TODO code smell, creating 1 BlockingQueue per inpoint
            }

            camelContext.addRouteDefinition(routeDefinition);
            pointRoutes.put(uri, routeDefinition);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot register route for point : " + point, e);
        }

        // TODO
        //        Float currentValue = plugin.getCurrentValue(point, camelContext);
        //        stateHolder.setState(point.getId(), currentValue);
    }

    public void removeInRoute(InPoint point) {
        RouteDefinition routeDefinition = pointRoutes.get(point.getUri());
        if (routeDefinition == null) {
            log.warn("trying to remove the route of a point that is not registered" + point);
            return;
        }

        try {
            camelContext.removeRouteDefinition(routeDefinition);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot remove route for point : " + point, e);
        }
    }

    @Override
    public void configure() throws Exception {
    }

}

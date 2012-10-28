package net.awired.housecream.server.router;

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

    @Inject
    private ModelCamelContext camelContext;

    @Inject
    private PluginService pluginService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<String, RouteDefinition> pointRoutes = Collections
            .synchronizedMap(new HashMap<String, RouteDefinition>());

    public void registerInRoute(InPoint point) {
        try {
            HousecreamPlugin plugin = pluginService.getPluginFromPrefix(point.extractUrlPrefix());
            RouteDefinition routeDefinition;
            if (plugin.isCommand()) {
                routeDefinition = from(point.getUrl()).to("direct:command");
            } else {
                routeDefinition = from(point.getUrl()).to(StaticRouteManager.EVENT_HOLDER_QUEUE);
            }

            camelContext.addRouteDefinition(routeDefinition);
            pointRoutes.put(point.getUrl(), routeDefinition);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot register route for point : " + point, e);
        }

        //        Float currentValue = plugin.getCurrentValue(point, camelContext);
        //        stateHolder.setState(point.getId(), currentValue);
    }

    public void removeInRoute(InPoint point) {
        RouteDefinition routeDefinition = pointRoutes.get(point.getUrl());
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

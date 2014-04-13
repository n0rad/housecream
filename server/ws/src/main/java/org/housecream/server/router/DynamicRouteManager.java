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
package org.housecream.server.router;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.housecream.plugins.api.HousecreamPlugin;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.service.PluginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DynamicRouteManager extends RouteBuilder {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CamelContext camelContext;

    @Autowired
    private InEventTransformer eventTransformer;

    @Autowired
    private PluginService pluginService;

    private Map<URI, RouteDefinition> pointRoutes = Collections.synchronizedMap(new HashMap<URI, RouteDefinition>());

    public void registerInRoute(Point point) {
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

    public void removeInRoute(Point point) {
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

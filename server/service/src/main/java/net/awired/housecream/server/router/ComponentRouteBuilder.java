package net.awired.housecream.server.router;

import net.awired.housecream.server.api.domain.Point;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

public class ComponentRouteBuilder extends RouteBuilder {

    private RouteDefinition routeDefinition;
    private final Point point;

    public ComponentRouteBuilder(Point point) {
        this.point = point;
    }

    @Override
    public void configure() throws Exception {
        routeDefinition = from(point.getUrl()).to(StaticRouteManager.EVENT_HOLDER_QUEUE);
    }

    public RouteDefinition getRouteDefinition() {
        return routeDefinition;
    }

}

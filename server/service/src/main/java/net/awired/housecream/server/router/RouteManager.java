package net.awired.housecream.server.router;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import net.awired.housecream.server.api.domain.Point;
import net.awired.housecream.server.engine.ConsequenceAction;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.StateHolder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RouteManager extends RouteBuilder {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public static final String EVENT_HOLDER_QUEUE = "seda:eventHolder";

    @Inject
    private ModelCamelContext camelContext;

    @Inject
    private EngineProcessor engineProcessor;

    @Inject
    private StateHolder stateHolder;

    @Inject
    private EngineResultSplitter splitter;

    @Inject
    private OutDynamicRouter dynamicRouter;

    private Map<String, RouteDefinition> pointRoutes = Collections
            .synchronizedMap(new HashMap<String, RouteDefinition>());

    public void registerPointRoute(Point point) {
        try {
            RouteDefinition routeDefinition = from(point.getUrl()).to(EVENT_HOLDER_QUEUE);
            camelContext.addRouteDefinition(routeDefinition);
            pointRoutes.put(point.getUrl(), routeDefinition);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot register route for point : " + point, e);
        }

        //        Float currentValue = plugin.getCurrentValue(point, camelContext);
        //        stateHolder.setState(point.getId(), currentValue);
    }

    public void removePointRoute(Point point) {
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

        from(EVENT_HOLDER_QUEUE).process(engineProcessor).split().method(splitter, "split").parallelProcessing()
                .to("direct:output");

        from("direct:output").inOut().dynamicRouter().method(dynamicRouter, "route").process(new Processor() {
            @Override
            public void process(Exchange arg0) throws Exception {
                ConsequenceAction action = arg0.getUnitOfWork().getOriginalInMessage()
                        .getHeader("ACTION", ConsequenceAction.class);
                stateHolder.setState(action.getPointId(), action.getValue());
            }
        });

    }

}

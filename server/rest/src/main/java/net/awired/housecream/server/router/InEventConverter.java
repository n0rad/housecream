package net.awired.housecream.server.router;

import net.awired.housecream.plugins.api.HousecreamPlugin;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InEventConverter implements Processor {

    private final Logger log = LoggerFactory.getLogger(getClass());
    public static final String PLUGIN_HEADER_NAME = "hc-plugin";
    public static final String INPOINT_HEADER_NAME = "hc-inpoint";

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            HousecreamPlugin plugin = exchange.getIn().getHeader(PLUGIN_HEADER_NAME, HousecreamPlugin.class);
            InPoint inpoint = exchange.getIn().getHeader(INPOINT_HEADER_NAME, InPoint.class);
            Float readInValue = plugin.readInValue(exchange.getIn());
            Event event = new Event();
            event.setPointId(inpoint.getId());
            event.setValue(readInValue);
            exchange.getIn().setBody(event);
        } catch (Exception e) {
            log.warn("Cannot convert in message to event", e);
            exchange.setProperty(Exchange.ROUTE_STOP, Boolean.TRUE);
        }
    }

}

package net.awired.housecream.server.router;

import java.util.Map;
import net.awired.ajsl.core.lang.Pair;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.engine.ConsequenceAction;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.Properties;
import org.apache.camel.impl.DefaultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OutDynamicRouter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String ROUTED_FLAG = "invoked";
    private static final String OUT_URL = "outUrl";

    public Message buildMessage(Pair<Object, Map<String, Object>> bodyAndHeaders, OutPoint outpoint,
            ConsequenceAction action) {
        DefaultMessage message = new DefaultMessage();
        message.setHeaders(bodyAndHeaders.right);
        message.setBody(bodyAndHeaders.left);
        message.setHeader("ACTION", action);
        message.setHeader(OutDynamicRouter.OUT_URL, outpoint.getUrl());
        return message;

    }

    public Object route(Exchange exchange, @Body Object body, @Properties Map<String, Object> properties,
            @Header(OUT_URL) String url) {
        exchange.setPattern(ExchangePattern.InOut);
        //        Object url = properties.get(OUT_URL);
        if (url == null) {
            log.error("Cannot found url for output message", body);
            return null;
        }

        int invoked = 0;
        Object current = properties.get(ROUTED_FLAG);
        if (current != null) {
            invoked = Integer.valueOf(current.toString());
        }
        invoked++;
        properties.put(ROUTED_FLAG, invoked);

        if (invoked == 1) {
            return url.toString();
        }
        return null;
    }
}

package net.awired.housecream.server.router;

import net.awired.housecream.server.engine.Action;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsequenceDelayer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public long calculateDelay(Exchange exchange) {
        long delayMili = exchange.getIn().getBody(Action.class).getDelayMili();
        log.debug("Delaying exchange {} for {} ms", exchange, delayMili);
        return delayMili;
    }

}

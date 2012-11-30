package net.awired.housecream.server.router;

import net.awired.housecream.server.engine.Action;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class ConsequenceDelayer {

    public long calculateDelay(Exchange exchange) {
        long delayMili = exchange.getIn().getBody(Action.class).getDelayMili();
        return delayMili;
    }

}

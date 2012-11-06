package net.awired.housecream.server.router;

import net.awired.housecream.server.api.domain.rule.Consequence;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class ConsequenceDelayer {

    public long calculateDelay(Exchange exchange) {
        long delayMili = exchange.getIn().getBody(Consequence.class).getDelayMili();
        return delayMili;
    }

}

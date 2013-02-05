package net.awired.housecream.server.command;

import net.awired.housecream.server.component.HcWebConsumer;
import net.awired.housecream.server.engine.OutEvent;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.springframework.stereotype.Component;

@Component
public class CommandService {

    private HcWebConsumer consumer;

    public void processOutEvent(OutEvent event) throws Exception {
        Exchange exchange = consumer.getEndpoint().createExchange(ExchangePattern.InOut);
        try {
            exchange.getIn().setBody(event);
            consumer.getProcessor().process(exchange);
            //TODO            return exchange.getOut().getBody();
        } catch (Exception e) {
            exchange.setException(e);
        }
        if (exchange.getException() != null) {
            throw exchange.getException();
        }
    }

    public void setConsumer(HcWebConsumer consumer) {
        this.consumer = consumer;
    }

}

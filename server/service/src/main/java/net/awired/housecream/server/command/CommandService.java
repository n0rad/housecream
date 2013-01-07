package net.awired.housecream.server.command;

import net.awired.housecream.server.component.HcWebConsumer;
import net.awired.housecream.server.engine.OutEvent;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.springframework.stereotype.Component;

@Component
public class CommandService {

    private HcWebConsumer consumer;

    public Object processOutEvent(OutEvent event) {
        Exchange exchange = consumer.getEndpoint().createExchange(ExchangePattern.InOut);
        try {
            exchange.getIn().setBody(event);
            consumer.getProcessor().process(exchange);
            return exchange.getOut().getBody();
        } catch (Exception e) {
            exchange.setException(e);
            throw new RuntimeException(e);
        }
    }

    public void setConsumer(HcWebConsumer consumer) {
        this.consumer = consumer;
    }

}

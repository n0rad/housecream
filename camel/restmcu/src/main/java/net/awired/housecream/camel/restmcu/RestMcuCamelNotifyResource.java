package net.awired.housecream.camel.restmcu;

import javax.ws.rs.Path;
import net.awired.restmcu.api.domain.board.RestMcuBoardNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultConsumer;

@Path("/")
public class RestMcuCamelNotifyResource implements RestMcuNotifyResource {

    private DefaultConsumer consumer;
    private RestMcuEndpoint endpoint;

    public RestMcuCamelNotifyResource(RestMcuEndpoint endpoint, RestMcuConsumer consumer) {
        this.endpoint = endpoint;
        this.consumer = consumer;
    }

    @Override
    public void lineNotification(RestMcuLineNotification pinNotification) {
        Exchange camelExchange = endpoint.createExchange(ExchangePattern.InOnly);
        Message in = camelExchange.getIn();
        in.setBody(pinNotification);
        try {
            consumer.getProcessor().process(camelExchange);
        } catch (Exception exception) {
            camelExchange.setException(exception);
        }
    }

    @Override
    public void boardNotification(RestMcuBoardNotification boardNotification) {
        Exchange camelExchange = endpoint.createExchange(ExchangePattern.InOnly);
        Message in = camelExchange.getIn();
        in.setBody(boardNotification);
        try {
            consumer.getProcessor().process(camelExchange);
        } catch (Exception exception) {
            camelExchange.setException(exception);
        }
    }

    @Override
    public long getPosixTime() {
        return System.currentTimeMillis() / 1000L;
    }

}

package net.awired.housecream.camel.restmcu;

import javax.ws.rs.Path;
import net.awired.restmcu.api.domain.board.RestMcuBoardNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;

@Path("/")
public class RestMcuCamelNotifyResource implements RestMcuNotifyResource {

    private RestMcuConsumer consumer;

    public RestMcuCamelNotifyResource(RestMcuConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void lineNotification(RestMcuLineNotification pinNotification) {
        //TODO check remote address to be sure it match the source
        Exchange camelExchange = consumer.getEndpoint().createExchange(ExchangePattern.InOnly);
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
        //TODO check remote address to be sure it match the source
        Exchange camelExchange = consumer.getEndpoint().createExchange(ExchangePattern.InOnly);
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

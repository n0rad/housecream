package net.awired.housecream.camel.restmcu;

import java.net.URI;
import net.awired.ajsl.web.rest.RestContext;
import net.awired.restmcu.api.resource.client.RestMcuLineResource;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

/**
 * The restmcu consumer.
 */
public class RestMcuPoolConsumer extends ScheduledPollConsumer {
    private final RestMcuEndpoint endpoint;
    private RestMcuLineResource restMcuClient;
    private int lineId;

    public RestMcuPoolConsumer(RestMcuEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        URI uri = endpoint.getEndpointConfiguration().getURI();

        String url = "http://" + uri.getHost();
        if (uri.getPort() != -1) {
            url += ":" + uri.getPort();
        }
        restMcuClient = new RestContext().prepareClient(RestMcuLineResource.class, url, null, true);
        lineId = Integer.valueOf(uri.getPath().substring(1));
        this.endpoint = endpoint;
    }

    @Override
    protected int poll() throws Exception {
        Exchange exchange = endpoint.createExchange();

        Float lineValue = restMcuClient.getLineValue(lineId);

        exchange.getIn().setBody(lineValue);

        try {
            // send message to next processor in the route
            getProcessor().process(exchange);
            return 1; // number of messages polled
        } finally {
            // log exception if an exception occurred and was not handled
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
}

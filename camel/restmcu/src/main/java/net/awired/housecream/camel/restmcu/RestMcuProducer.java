package net.awired.housecream.camel.restmcu;

import java.net.URI;
import net.awired.ajsl.web.rest.RestContext;
import net.awired.restmcu.api.resource.client.RestMcuLineResource;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The restmcu producer.
 */
public class RestMcuProducer extends DefaultProducer {
    private static final transient Logger LOG = LoggerFactory.getLogger(RestMcuProducer.class);
    private RestMcuEndpoint endpoint;
    private RestMcuLineResource restMcuClient;
    private int lineId;

    public RestMcuProducer(RestMcuEndpoint endpoint) {
        super(endpoint);
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
    public void process(Exchange exchange) throws Exception {
        Float value = (Float) exchange.getIn().getBody();
        restMcuClient.setLineValue(lineId, value);
    }

}

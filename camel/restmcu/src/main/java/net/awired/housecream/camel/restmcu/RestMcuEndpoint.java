package net.awired.housecream.camel.restmcu;

import java.net.URI;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * restmcu://localhost:8976/[lineId]?
 */
public class RestMcuEndpoint extends DefaultEndpoint {

    public RestMcuEndpoint() {
    }

    public RestMcuEndpoint(String uri, RestMcuComponent component) {
        super(uri, component);
    }

    public RestMcuEndpoint(String endpointUri) {
        super(endpointUri);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new RestMcuProducer(this);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new RestMcuConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getRestMcuUrl() {
        URI uri = getEndpointConfiguration().getURI();
        String url = "http://" + uri.getHost();
        if (uri.getPort() != -1) {
            url += ":" + uri.getPort();
        }
        return url;
    }

}
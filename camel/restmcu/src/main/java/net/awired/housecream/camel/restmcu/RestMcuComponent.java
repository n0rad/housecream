package net.awired.housecream.camel.restmcu;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link RestMcuEndpoint}.
 */
public class RestMcuComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new RestMcuEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}

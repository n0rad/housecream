package net.awired.housecream.camel.solar;

import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

public class SolarComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new SolarEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }

}

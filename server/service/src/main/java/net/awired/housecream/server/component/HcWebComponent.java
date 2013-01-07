package net.awired.housecream.server.component;

import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

public class HcWebComponent extends DefaultComponent {

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        HcWebEndpoint endpoint = new HcWebEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}

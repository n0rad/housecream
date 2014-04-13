package org.housecream.plugins.openweathermap;

import java.util.Map;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

public class OpenWeatherMapComponent extends DefaultComponent {
    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new OpenWeatherMapEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}

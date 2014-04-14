package org.housecream.plugins.openweathermap;

import java.net.URI;
import java.net.URL;
import java.util.Map;
import javax.validation.ValidationException;
import org.apache.camel.Endpoint;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultComponent;
import org.apache.commons.lang3.tuple.Pair;
import org.housecream.plugins.api.HousecreamPlugin;
import org.housecream.server.api.domain.Plugin;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.rule.Consequence;

public class OpenWeatherMapComponent extends DefaultComponent implements HousecreamPlugin {

    private Plugin plugin = Plugin.plugin("openWeatherMap", "Open Weather Map",
            "The OpenWeatherMap service provides free weather data and forecast API suitable for any cartographic services like web and smartphones applications. Ideology is inspired by OpenStreetMap and Wikipedia that make information free and available for everybody. OpenWeatherMap provides wide range of weather data such as map with current weather, week forecast, precipitation, wind, clouds, data from weather Stations and many others. Weather data is received from global Meteorological broadcast services and more than <a href=\"http://openweathermap.org/sys\">40 000 weather stations.</a>");

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new OpenWeatherMapEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }

    @Override
    public Plugin plugin() {
        return plugin;
    }

    @Override
    public URL getLogo() {
        return getClass().getResource("openweathermap.png");
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public URI validateAndNormalizeUri(URI pointUri) throws ValidationException {
        return null;
    }

    @Override
    public Pair<Object, Map<String, Object>> prepareOutBodyAndHeaders(Consequence action, Point point) {
        return null;
    }

    @Override
    public Float readValue(Message in) throws Exception {
        return null;
    }
}

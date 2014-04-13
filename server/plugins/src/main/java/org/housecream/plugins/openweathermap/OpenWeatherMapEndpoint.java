package org.housecream.plugins.openweathermap;

import static org.housecream.plugins.openweathermap.OpenWeatherMapDataType.valueOfName;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.housecream.plugins.openweathermap.OpenWeatherMapResource.Language;
import org.housecream.plugins.openweathermap.OpenWeatherMapResource.Unit;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import fr.norad.jaxrs.client.server.rest.RestBuilder;
import fr.norad.jaxrs.client.server.rest.RestBuilder.Generic;
import lombok.Data;

/**
 * openweathermap:wind/current?latitude=42&longitude=43
 * openweathermap:wind/forecast/hours/3?latitude=42&longitude=43
 * openweathermap:wind/forecast/days/3?latitude=42&longitude=43
 */
@Data
public class OpenWeatherMapEndpoint extends DefaultEndpoint {

    private static final long resultCacheRetention = 60 * 60 * 1000; // 1h

    private OpenWeatherMapResource client;

    private Unit unit = Unit.metric;
    private Location location = new Location();
    private Language language;
    private OpenWeatherMapDataType type;
    private Integer forecastDays;
    private Integer forecastHours;

    private static Map<Location, OpenWeatherMapResultCurrent> currentCache = new ConcurrentHashMap<>();
    private static Map<Location, OpenWeatherMapResultForecast> daysForecastCache = new ConcurrentHashMap<>();
    private static Map<Location, OpenWeatherMapResultForecast> hoursForecastCache = new ConcurrentHashMap<>();

    Pattern pattern = Pattern.compile("/(?<when>current|forecast)(/(?<unit>days|hours)/(?<num>\\d))?");

    public OpenWeatherMapEndpoint(String uri, OpenWeatherMapComponent component) {
        super(uri, component);
        client = RestBuilder.rest().threadSafe(true)
                .addProvider(new JacksonJaxbJsonProvider())
                .addOutInterceptor(Generic.outStdoutLogger)
                .addInInterceptor(Generic.inStdoutLogger)
                .buildClient(OpenWeatherMapResource.class, OpenWeatherMapResource.OPENWEATHERMAP_API_URL);

        this.type = valueOfName(getEndpointConfiguration().getURI().getHost());
        ParsePath();
    }

    private void ParsePath() {
        String path = getEndpointConfiguration().getURI().getPath();
        Matcher matcher = pattern.matcher(path);
        if (!matcher.matches()) {
            throw new IllegalStateException("Path is not valid, received " + path + " but should be like : " + pattern.pattern());
        }
        if ("forecast".equals(matcher.group("when"))) {
            int num = Integer.parseInt(matcher.group("num"));
            if ("days".equals(matcher.group("unit"))) {
                forecastDays = num;
            } else {
                forecastHours = num;
            }
        }
    }

    @Override
    public Producer createProducer() throws Exception {
        return null;
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new OpenWeatherMapConsumer(this, processor);
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    private boolean isExpired(long date, long retention) {
        return System.currentTimeMillis() > date * 1000L + retention;
    }

    public OpenWeatherMapResultCurrent getCurrentWeather() {
        if (currentCache.get(location) == null || isExpired(currentCache.get(location).getDt(), resultCacheRetention)) {
            OpenWeatherMapResultCurrent currentWeather = client
                    .getCurrentWeather(location.getCityName(),
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getCityId(),
                            unit,
                            language);
            if (currentWeather.getDt() == null) {
                currentWeather.setDt(System.currentTimeMillis() / 1000);
            }
            currentCache.put(location, currentWeather);
        }
        return currentCache.get(location);
    }

    public OpenWeatherMapResultForecast getDaysForecastWeather() {
        if (daysForecastCache.get(location) == null || isExpired(daysForecastCache.get(location).getDt(), resultCacheRetention)) {
            OpenWeatherMapResultForecast dailyForecast = client
                    .getDailyForecast(location.getCityName(),
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getCityId(),
                            unit,
                            language);
            dailyForecast.setDt(System.currentTimeMillis() / 1000);
            daysForecastCache.put(location, dailyForecast);
        }
        return daysForecastCache.get(location);
    }

    public OpenWeatherMapResultForecast getHoursForecastWeather() {
        if (hoursForecastCache.get(location) == null || isExpired(hoursForecastCache.get(location).getDt(), resultCacheRetention)) {
            OpenWeatherMapResultForecast forecast = client
                    .getForecast(location.getCityName(),
                            location.getLatitude(),
                            location.getLongitude(),
                            location.getCityId(),
                            unit,
                            language);
            forecast.setDt(System.currentTimeMillis() / 1000);
            hoursForecastCache.put(location, forecast);
        }
        return hoursForecastCache.get(location);
    }

    @Data
    public class Location {
        private String cityName;
        private Float latitude;
        private Float longitude;
        private Integer cityId;
    }

    //

    public void setCityName(String cityName) {
        this.location.cityName = cityName;
    }

    public void setLatitude(Float latitude) {
        this.location.latitude = latitude;
    }

    public void setLongitude(Float longitude) {
        this.location.longitude = longitude;
    }

    public void setCityId(Integer cityId) {
        this.location.cityId = cityId;
    }


}

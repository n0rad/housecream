package org.housecream.plugins.openweathermap;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;

public class OpenWeatherMapConsumerRunner implements Runnable {

    private OpenWeatherMapEndpoint endpoint;
    private OpenWeatherMapConsumer consumer;

    public OpenWeatherMapConsumerRunner(OpenWeatherMapConsumer consumer) {
        this.consumer = consumer;
        this.endpoint = (OpenWeatherMapEndpoint) consumer.getEndpoint();
    }

    @Override
    public void run() {
        Object data;
        if (endpoint.getForecastDays() != null) {
            data = extractRequestedData(endpoint.getDaysForecastWeather());
        } else if (endpoint.getForecastHours() != null) {
            data = extractRequestedData(endpoint.getHoursForecastWeather());
        } else {
            data = extractRequestedData(endpoint.getCurrentWeather());
        }

        Exchange camelExchange = endpoint.createExchange(ExchangePattern.InOnly);
        camelExchange.getIn().setBody(data);
        try {
            consumer.getProcessor().process(camelExchange);
        } catch (Exception exception) {
            camelExchange.setException(exception);
        }

    }

    public Object extractRequestedData(OpenWeatherMapResultForecast result) {
        switch (endpoint.getType()) {
            case wind:
                return result.getWind();
            case temperature:
                return result.getMain().getTemperature();
            case max_temperature:
                return result.getMain().getTemperatureMax();
            case min_temperature:
                return result.getMain().getTemperatureMin();
            case sunrise:
                return result.getSystem().getSunrise();
            case sunset:
                return result.getSystem().getSunset();
            case condition:
                ////////////////////////////////////////////////
            case pressure:
                return result.getMain().getPressure();
            case humidity:
                return result.getMain().getHumidity();
            default:
                throw new IllegalStateException("Don't know how to get value of type :" + endpoint.getType());
        }
    }

    public Object extractRequestedData(OpenWeatherMapResultCurrent result) {
        switch (endpoint.getType()) {
            case wind:
                return result.getWind();
            case temperature:
                return result.getMain().getTemperature();
            case max_temperature:
                return result.getMain().getTemperatureMax();
            case min_temperature:
                return result.getMain().getTemperatureMin();
            case sunrise:
                return result.getSystem().getSunrise();
            case sunset:
                return result.getSystem().getSunset();
            case condition:
                ////////////////////////////////////////////////
            case pressure:
                return result.getMain().getPressure();
            case humidity:
                return result.getMain().getHumidity();
            default:
                throw new IllegalStateException("Don't know how to get value of type :" + endpoint.getType());
        }
    }

}

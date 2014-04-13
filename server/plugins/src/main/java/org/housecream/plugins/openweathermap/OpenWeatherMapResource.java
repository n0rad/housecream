package org.housecream.plugins.openweathermap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/")
public interface OpenWeatherMapResource {
    static final String OPENWEATHERMAP_API_URL = "http://api.openweathermap.org/data/2.5";

    enum Unit {
        metric, imperial;
    }

    //  @PathParam("mode") Mode mode, : json and xml are really different
    enum Mode {
        xml, json, html;
    }

    enum Language {
        en, ru, it, sp, ua, de, pt, ro, pl, fi, nl, fr, bg, se, zh_tw, zh_cn, tr;
    }

    @GET
    @Path("/weather")
    OpenWeatherMapResultCurrent getCurrentWeather(@QueryParam("q") String cityQuery,
                                                  @QueryParam("lat") Float latitude,
                                                  @QueryParam("lon") Float longitude,
                                                  @QueryParam("id") Integer cityId,
                                                  @QueryParam("units") Unit units,
                                                  @QueryParam("lang") Language lang);

    @GET
    @Path("/forecast")
    OpenWeatherMapResultForecast getForecast(@QueryParam("q") String cityQuery,
                                             @QueryParam("lat") Float latitude,
                                             @QueryParam("lon") Float longitude,
                                             @QueryParam("id") Integer cityId,
                                             @QueryParam("units") Unit units,
                                             @QueryParam("lang") Language lang);

    @GET
    @Path("/forecast/daily")
    OpenWeatherMapResultForecast getDailyForecast(@QueryParam("q") String cityQuery,
                                                  @QueryParam("lat") Float latitude,
                                                  @QueryParam("lon") Float longitude,
                                                  @QueryParam("id") Integer cityId,
                                                  @QueryParam("units") Unit units,
                                                  @QueryParam("lang") Language lang);
}

package org.housecream.plugins.openweathermap;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

@Data
@XmlRootElement
public class OpenWeatherMapResultCurrent {
    @XmlElement(name = "message")
    private Integer errorMessage;
    @XmlElement(name = "cod")
    private Integer resultCode;

    @XmlElement(name = "coord")
    private Coordinate coordinate;
    @XmlElement(name = "sys")
    private System system;
    @XmlElement(name = "weather")
    private List<Weather> weathers;
    private String base;
    private Main main;
    private Wind wind;
    private Clouds clouds;
    private Long dt;
    private String dt_txt;
    private Long id;
    private String name;

    ////////////////

    @Data
    public static class Clouds {
        private Integer all;
    }

    @Data
    public static class Wind {
        private Float speed;
        private Float gust;
        @XmlElement(name = "deg")
        private Integer degree;
    }

    @Data
    public static class Main {
        @XmlElement(name = "temp")
        private Float temperature;
        private Integer humidity;
        private Integer pressure;
        @XmlElement(name = "temp_min")
        private Float temperatureMin;
        @XmlElement(name = "temp_max")
        private Float temperatureMax;
        @XmlElement(name = "temp_kf")
        private Float temperatureKf;
        @XmlElement(name = "sea_level")
        private Float seaLevel;
        @XmlElement(name = "grnd_level")
        private Float groundLevel;
    }

    @Data
    public static class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    public static class System {
        private Float message;
        private String country;
        private Long sunrise;
        private Long sunset;
        private String pod;
    }

    @Data
    public static class Coordinate {
        @XmlElement(name = "lon")
        private Float longitude;
        @XmlElement(name = "lat")
        private Float latitude;
    }

}


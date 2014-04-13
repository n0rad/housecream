package org.housecream.plugins.openweathermap;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.housecream.plugins.openweathermap.OpenWeatherMapResultCurrent.Coordinate;
import lombok.Data;

@Data
@XmlRootElement
public class OpenWeatherMapResultForecast {
    private String cod;
    private Float message;
    private City city;
    private Integer cnt;
    private List<OpenWeatherMapResultCurrent> list;
    private long dt;
    //

    @Data

    public class City {
        private Long id;
        private String name;
        @XmlElement(name = "coord")
        private Coordinate coordinate;
        private String country;
        private Long population;
        private System sys;
    }

    @Data
    public class System {
        private Long population;
    }
}

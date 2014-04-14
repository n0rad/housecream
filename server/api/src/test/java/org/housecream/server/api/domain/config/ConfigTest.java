package org.housecream.server.api.domain.config;

import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ConfigTest {
    @Test
    public void should_set_types() {
        Config config = new Config();
        config.setValue("timeZone", "Europe/London");
        config.setValue("temperatureUnit", "Kelvin");
        config.setValue("speedUnit", "MPerHour");

        Assertions.assertThat(config.getTimeZone()).isEqualTo(TimeZone.getTimeZone("Europe/London"));
        Assertions.assertThat(config.getTemperatureUnit()).isEqualTo(TemperatureUnit.Kelvin);
        Assertions.assertThat(config.getSpeedUnit()).isEqualTo(SpeedUnit.MPerHour);
    }
}

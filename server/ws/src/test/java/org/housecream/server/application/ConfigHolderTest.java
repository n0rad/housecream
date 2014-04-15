package org.housecream.server.application;


import java.util.Set;
import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.housecream.plugins.gmail.GoogleServiceConfig;
import org.housecream.server.api.domain.config.Config;
import org.housecream.server.api.domain.config.PropertyDefinition;
import org.housecream.server.api.domain.config.SpeedUnit;
import org.housecream.server.api.domain.config.TemperatureUnit;
import org.junit.Test;

public class ConfigHolderTest {

    ConfigHolder configHolder = new ConfigHolder();

    @Test
    public void should_get_default_value() {
        configHolder.setValue("securitySeedLength", "42");

        GoogleServiceConfig configObject = configHolder.getConfigObject(GoogleServiceConfig.class);

        Assertions.assertThat(configObject.getSecuritySeedLength()).isEqualTo(42);
    }

    @Test
    public void should() {
        GoogleServiceConfig configObject = configHolder.getConfigObject(GoogleServiceConfig.class);

        Set<PropertyDefinition> propertiesDefinition = configHolder.getPropertiesDefinition();

        Assertions.assertThat(propertiesDefinition).isNotNull();
    }

    @Test
    public void should_set_types() {
        Config config = new Config();
//        config.setValue("timeZone", "Europe/London");
//        config.setValue("temperatureUnit", "Kelvin");
//        config.setValue("speedUnit", "MPerHour");

        Assertions.assertThat(config.getTimeZone()).isEqualTo(TimeZone.getTimeZone("Europe/London"));
        Assertions.assertThat(config.getTemperatureUnit()).isEqualTo(TemperatureUnit.Kelvin);
        Assertions.assertThat(config.getSpeedUnit()).isEqualTo(SpeedUnit.MPerHour);
    }
}

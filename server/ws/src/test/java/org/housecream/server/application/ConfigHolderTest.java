package org.housecream.server.application;


import java.util.Map;
import java.util.TimeZone;
import org.assertj.core.api.Assertions;
import org.housecream.plugins.gmail.GoogleServiceConfig;
import org.housecream.server.api.domain.config.Config;
import org.housecream.server.api.domain.config.SpeedUnit;
import org.housecream.server.api.domain.config.TemperatureUnit;
import org.housecream.server.api.domain.config.configDefinition;
import org.junit.Test;
import lombok.Data;

public class ConfigHolderTest {

    ConfigHolder configHolder = new ConfigHolder();

    @Test
    public void should_get_config_values() {
        configHolder.getConfigObject(Config.class);
        configHolder.setValue("securitySeedLength", "42");

        Map<String, Object> configValues = configHolder.getConfigValues();

        Assertions.assertThat(configValues.size()).isGreaterThan(1);
        Assertions.assertThat(configValues.get("securitySeedLength")).isEqualTo(42);
    }

    @Test
    public void should_import_config_values_to_new_config_object() {
        configHolder.setValue("securitySeedLength", "42");

        GoogleServiceConfig configObject = configHolder.getConfigObject(GoogleServiceConfig.class);

        Assertions.assertThat(configObject.getSecuritySeedLength()).isEqualTo(42);
    }

    @Test
    public void should_get_definition_merged_for_all_types() {
        configHolder.getConfigObject(OtherConfig1.class);
        configHolder.getConfigObject(OtherConfig2.class);

        configDefinition configDefinition = configHolder.getConfigDefinition();

        Assertions.assertThat(configDefinition.getProperties()).containsKeys("genre", "style");
        Assertions.assertThat(configDefinition.getProperties().get("genre").getDefaultValue()).isEqualTo("salut");
        Assertions.assertThat(configDefinition.getProperties().get("style").getDefaultValue()).isEqualTo("ouda");
        Assertions.assertThat(configDefinition.getProperties().get("genre").getExtras().get("Group")).isEqualTo(OtherConfig1.class);
        Assertions.assertThat(configDefinition.getProperties().get("style").getExtras().get("Group")).isEqualTo(OtherConfig2.class);
    }

    @Test
    public void should_set_types() {
        configHolder.setValue("timeZone", "Europe/London");
        configHolder.setValue("temperatureUnit", "Kelvin");
        configHolder.setValue("speedUnit", "MPerHour");

        Config config = configHolder.getConfigObject(Config.class);

        Assertions.assertThat(config.getTimeZone()).isEqualTo(TimeZone.getTimeZone("Europe/London"));
        Assertions.assertThat(config.getTemperatureUnit()).isEqualTo(TemperatureUnit.Kelvin);
        Assertions.assertThat(config.getSpeedUnit()).isEqualTo(SpeedUnit.MPerHour);
    }


    @Data
    public static class OtherConfig1 extends Config {
        private String genre = "salut";
    }

    @Data
    public static class OtherConfig2 extends Config {
        private String style = "ouda";
    }

}

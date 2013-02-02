package net.awired.housecream.camel.solar;

import static org.fest.assertions.Assertions.assertThat;
import net.awired.housecream.camel.solar.OLD.SolarAdvancedCalculator;
import net.awired.housecream.camel.solar.OLD.SolarTimePhase;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import com.luckycatlabs.sunrisesunset.dto.Location;

public class SolarAdvancedCalculatorTest {

    private static final DateTimeZone ZONE = DateTimeZone.forID("America/New_York");
    private SolarAdvancedCalculator calculator = new SolarAdvancedCalculator(
            new Location("39.9522222", "-75.1641667"), ZONE.toTimeZone());

    @Test
    public void should_calculate_astronomical_dawn_for_day() throws Exception {
        DateTime date = calculator.calculateStartingPhaseDate(SolarTimePhase.ASTRONOMICAL_DAWN_TWILIGHT,
                new DateTime(2013, 1, 1, 0, 0, ZONE));

        assertThat(date.isEqual(new DateTime(2013, 1, 1, 5, 45, ZONE))).isTrue();
    }

    @Test
    public void should_calculate_nautical_dawn_for_day() throws Exception {
        DateTime date = calculator.calculateStartingPhaseDate(SolarTimePhase.NAUTICAL_DAWN_TWILIGHT, new DateTime(
                2013, 1, 1, 0, 0, ZONE));

        assertThat(date.isEqual(new DateTime(2013, 1, 1, 6, 18, ZONE))).isTrue();
    }

    @Test
    public void should_calculate_civil_dawn_for_day() throws Exception {
        DateTime date = calculator.calculateStartingPhaseDate(SolarTimePhase.CIVIL_DAWN_TWILIGHT, new DateTime(2013,
                1, 1, 0, 0, ZONE));

        assertThat(date.isEqual(new DateTime(2013, 1, 1, 6, 52, ZONE))).isTrue();
    }

    @Test
    public void should_calculate_sunrise_for_day() throws Exception {
        DateTime date = calculator.calculateStartingPhaseDate(SolarTimePhase.SUNRISE, new DateTime(2013, 1, 1, 0, 0,
                ZONE));

        assertThat(date.isEqual(new DateTime(2013, 1, 1, 7, 23, ZONE))).isTrue();
    }

    //

    @Test
    public void should_calculate_sunset_for_day() throws Exception {
        DateTime date = calculator.calculateStartingPhaseDate(SolarTimePhase.SUNSET, new DateTime(2013, 1, 1, 0, 0,
                ZONE));

        assertThat(date.isEqual(new DateTime(2013, 1, 1, 16, 46, ZONE))).isTrue();
    }

    @Test
    public void should_calculate_civil_dusk_for_day() throws Exception {
        DateTime date = calculator.calculateStartingPhaseDate(SolarTimePhase.CIVIL_DUSK_TWILIGHT, new DateTime(2013,
                1, 1, 0, 0, ZONE));

        assertThat(date.isEqual(new DateTime(2013, 1, 1, 17, 17, ZONE))).isTrue();
    }

    @Test
    public void should_calculate_nautical_dusk_for_day() throws Exception {
        DateTime date = calculator.calculateStartingPhaseDate(SolarTimePhase.NAUTICAL_DUSK_TWILIGHT, new DateTime(
                2013, 1, 1, 0, 0, ZONE));

        assertThat(date.isEqual(new DateTime(2013, 1, 1, 17, 51, ZONE))).isTrue();
    }

    @Test
    public void should_calculate_astronomical_dusk_for_day() throws Exception {
        DateTime date = calculator.calculateStartingPhaseDate(SolarTimePhase.ASTRONOMICAL_DUSK_TWILIGHT,
                new DateTime(2013, 1, 1, 0, 0, ZONE));

        assertThat(date.isEqual(new DateTime(2013, 1, 1, 18, 23, ZONE))).isTrue();
    }

}

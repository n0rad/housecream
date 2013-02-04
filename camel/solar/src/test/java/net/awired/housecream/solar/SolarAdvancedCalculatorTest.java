package net.awired.housecream.solar;

import static net.awired.housecream.solar.enumeration.SolarTimeEvent.astronomicalDawn;
import static net.awired.housecream.solar.enumeration.SolarTimeEvent.noon;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.JODA_TIME.assertThat;
import java.util.ArrayList;
import java.util.Arrays;
import net.awired.housecream.solar.enumeration.SolarTimeEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import com.google.common.collect.Lists;

public class SolarAdvancedCalculatorTest {

    private static final DateTimeZone ZONE = DateTimeZone.forID("America/New_York");
    private SolarAdvancedCalculator calculator = new SolarAdvancedCalculator(39.9522222, -75.1641667,
            ZONE.toTimeZone());

    @Test
    public void should_not_fail_for_empty_list() throws Exception {
        calculator.findNextEvent(new DateTime(), null);
        calculator.findNextEvent(new DateTime(), new ArrayList<SolarTimeEvent>());
    }

    @Test
    public void should_find_next_astronomical_dawn() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, ZONE);

        SolarState event = calculator.findNextEvent(date, Lists.newArrayList(astronomicalDawn));

        assertThat(event.getDate()).isEqualTo(new DateTime(2013, 1, 1, 5, 46, 33, 237, ZONE));
    }

    @Test
    public void should_find_astronomical_of_tomorrow_if_past_for_today() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 11, 0, 0, ZONE);

        SolarState event = calculator.findNextEvent(date, Lists.newArrayList(astronomicalDawn));

        assertThat(event.getDate()).isEqualTo(new DateTime(2013, 1, 2, 5, 46, 43, 244, ZONE));
    }

    @Test
    public void should_find_first_of_2_events_reverse_order() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, ZONE);

        SolarState event = calculator.findNextEvent(date, Arrays.asList(noon, astronomicalDawn));

        assertThat(event.getEvent()).isSameAs(astronomicalDawn);
        assertThat(event.getDate()).isEqualTo(new DateTime(2013, 1, 1, 5, 46, 33, 237, ZONE));
    }

    @Test
    public void should_find_first_of_2_events_both_past_for_today() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 18, 0, 0, ZONE);

        SolarState event = calculator.findNextEvent(date, Arrays.asList(noon, astronomicalDawn));

        assertThat(event.getEvent()).isSameAs(astronomicalDawn);
        assertThat(event.getDate()).isEqualTo(new DateTime(2013, 1, 2, 5, 46, 43, 244, ZONE));
    }

    @Test
    public void should_find_very_far_event() throws Exception {
        SolarAdvancedCalculator calculator = new SolarAdvancedCalculator(84.786525, -0.1275, ZONE.toTimeZone());
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, ZONE);

        SolarState event = calculator.findNextEvent(date, Arrays.asList(SolarTimeEvent.civilDawn));

        assertThat(event.getEvent()).isSameAs(SolarTimeEvent.civilDawn);
        assertThat(event.getDate()).isEqualTo(new DateTime(2013, 2, 19, 6, 59, 5, 447, ZONE));
    }

    @Test
    public void should_find_very_far_event_2() throws Exception {
        SolarAdvancedCalculator calculator = new SolarAdvancedCalculator(84.786525, -0.1275, ZONE.toTimeZone());
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, ZONE);

        SolarState event = calculator.findNextEvent(date, Arrays.asList(SolarTimeEvent.morningGoldenHourEnd));

        assertThat(event.getEvent()).isSameAs(SolarTimeEvent.morningGoldenHourEnd);
        assertThat(event.getDate()).isEqualTo(new DateTime(2013, 3, 23, 6, 46, 56, 881, ZONE));
    }
}

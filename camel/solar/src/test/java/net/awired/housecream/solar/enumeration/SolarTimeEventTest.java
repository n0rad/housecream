package net.awired.housecream.solar.enumeration;

import static org.fest.assertions.api.Assertions.assertThat;
import org.junit.Test;

public class SolarTimeEventTest {

    @Test
    public void should_find_previous_in_middle() throws Exception {
        assertThat(SolarTimeEvent.civilDawn.findPrevious()).isEqualTo(SolarTimeEvent.nauticalDawn);
    }

    @Test
    public void should_find_previous_at_end() throws Exception {
        assertThat(SolarTimeEvent.astronomicalDusk.findPrevious()).isEqualTo(SolarTimeEvent.nauticalDusk);
    }

    @Test
    public void should_find_previous_at_beginning() throws Exception {
        assertThat(SolarTimeEvent.nadir.findPrevious()).isEqualTo(SolarTimeEvent.astronomicalDusk);
    }

    @Test
    public void should_find_previous_at_beginning_plus_one() throws Exception {
        assertThat(SolarTimeEvent.astronomicalDawn.findPrevious()).isEqualTo(SolarTimeEvent.nadir);
    }

}

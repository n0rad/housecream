/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.plugins.solar;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.housecream.plugins.solar.enumeration.SolarTimeEvent.astronomicalDawn;
import static org.joda.time.DateTimeZone.UTC;
import org.housecream.plugins.solar.enumeration.SolarTimeEvent;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

public class SolarCalculatorTest {

    @Test
    public void should_find_all_event_for_midnight_london_UTC() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, UTC);
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.nadir, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 0, 5, 17, 257, UTC));
        }
        {
            long time = SolarCalculator.getTime(astronomicalDawn, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 6, 3, 58, 268, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.nauticalDawn, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 6, 44, 36, 304, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.civilDawn, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 7, 27, 31, 80, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.sunriseStart, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 8, 7, 27, 956, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.sunriseEnd, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 8, 11, 48, 647, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.morningGoldenHourEnd, date.getMillis(), 51.507222,
                    -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 9, 8, 11, 603, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.noon, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 12, 5, 17, 257, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.afternoonGoldenHourStart, date.getMillis(), 51.507222,
                    -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 15, 2, 22, 911, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.sunsetStart, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 15, 58, 45, 867, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.sunsetEnd, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 16, 3, 6, 557, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.civilDusk, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 16, 43, 3, 434, UTC));
        }
        {
            long time = SolarCalculator.getTime(SolarTimeEvent.nauticalDusk, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 17, 25, 58, 210, UTC));
        }
        {
            long time = SolarCalculator
                    .getTime(SolarTimeEvent.astronomicalDusk, date.getMillis(), 51.507222, -0.1275);
            assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 18, 6, 36, 246, UTC));
        }
    }

    @Test
    public void should_find_current_day_at_midnight_london_UTC() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, UTC);

        long time = SolarCalculator.getTime(SolarTimeEvent.astronomicalDawn, date.getMillis(), 51.507222, -0.1275);

        assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 6, 3, 58, 268, UTC));
    }

    @Test
    public void should_find_next_day_at_midnight_london_UTC() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, UTC);

        long time = SolarCalculator.getTime(SolarTimeEvent.astronomicalDawn, date.plusDays(1).getMillis(), 51.507222,
                -0.1275);

        assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 2, 6, 3, 59, 147, UTC));
    }

    @Test
    public void should_find_current_day_before_midnight_london_UTC() throws Exception {
        DateTime date = new DateTime(2012, 12, 31, 23, 59, UTC);

        long time = SolarCalculator.getTime(SolarTimeEvent.astronomicalDawn, date.getMillis(), 51.507222, -0.1275);

        assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2012, 12, 31, 6, 3, 54, 823, UTC));
    }

    @Test
    public void should_find_position_at_noon_for_london_UTC() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, UTC);

        long time = SolarCalculator.getTime(SolarTimeEvent.noon, date.getMillis(), 51.507222, -0.1275);
        SolarPosition position = SolarCalculator.getPosition(time, 51.507222, -0.1275);

        assertThat(position.getAltitude()).isEqualTo(0.2705335502287739);
        assertThat(position.getAzimuth()).isEqualTo(0.001614040552312632);
        assertThat(position.getAzimuth() * 180 / Math.PI).isEqualTo(0.09247771161047817);
    }

    @Test
    public void should_find_position_for_london_UTC2() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, UTC);

        long time = SolarCalculator.getTime(SolarTimeEvent.sunsetStart, date.getMillis(), 51.507222, -0.1275);
        SolarPosition position = SolarCalculator.getPosition(time, 51.507222, -0.1275);

        assertThat(position.getAltitude()).isEqualTo(-0.005691512891756371);
        assertThat(position.getAzimuth()).isEqualTo(0.9019716732292741);
        assertThat(position.getAzimuth() * 180 / Math.PI).isEqualTo(51.679170116390424);
    }

    @Test
    public void should_find_astronomicalDawn_at_midnight_NY() throws Exception {
        DateTimeZone ZONE = DateTimeZone.forID("America/New_York");

        DateTime date = new DateTime(2013, 1, 2, 0, 0, 0, ZONE);

        long time = SolarCalculator.getTime(SolarTimeEvent.astronomicalDawn, date.getMillis(), 39.9522222,
                -75.1641667);

        assertThat(new DateTime(time, UTC).toDateTime(ZONE))
                .isEqualTo(new DateTime(2013, 1, 2, 5, 46, 43, 244, ZONE));
    }

    @Test
    public void should_not_find_non_existing_event_for_this_position() throws Exception {
        DateTime date = new DateTime(2013, 1, 1, 0, 0, 0, UTC);

        long time = SolarCalculator.getTime(SolarTimeEvent.nadir, date.getMillis(), 84.786525, -0.1275);
        long time1 = SolarCalculator.getTime(SolarTimeEvent.astronomicalDawn, date.getMillis(), 84.786525, -0.1275);
        long time2 = SolarCalculator.getTime(SolarTimeEvent.nauticalDawn, date.getMillis(), 84.786525, -0.1275);
        long time3 = SolarCalculator.getTime(SolarTimeEvent.civilDawn, date.getMillis(), 84.786525, -0.1275);
        long time4 = SolarCalculator.getTime(SolarTimeEvent.sunriseStart, date.getMillis(), 84.786525, -0.1275);
        long time5 = SolarCalculator.getTime(SolarTimeEvent.sunriseEnd, date.getMillis(), 84.786525, -0.1275);
        long time6 = SolarCalculator.getTime(SolarTimeEvent.morningGoldenHourEnd, date.getMillis(), 84.786525,
                -0.1275);
        long time7 = SolarCalculator.getTime(SolarTimeEvent.noon, date.getMillis(), 84.786525, -0.1275);
        long time8 = SolarCalculator.getTime(SolarTimeEvent.afternoonGoldenHourStart, date.getMillis(), 84.786525,
                -0.1275);
        long time9 = SolarCalculator.getTime(SolarTimeEvent.sunsetStart, date.getMillis(), 84.786525, -0.1275);
        long time10 = SolarCalculator.getTime(SolarTimeEvent.sunsetEnd, date.getMillis(), 84.786525, -0.1275);
        long time11 = SolarCalculator.getTime(SolarTimeEvent.civilDusk, date.getMillis(), 84.786525, -0.1275);
        long time12 = SolarCalculator.getTime(SolarTimeEvent.nauticalDusk, date.getMillis(), 84.786525, -0.1275);
        long time13 = SolarCalculator.getTime(SolarTimeEvent.astronomicalDusk, date.getMillis(), 84.786525, -0.1275);

        assertThat(new DateTime(time, UTC)).isEqualTo(new DateTime(2013, 1, 1, 0, 5, 17, 257, UTC));
        assertThat(new DateTime(time1, UTC)).isEqualTo(new DateTime(2013, 1, 1, 10, 57, 8, 713, UTC));
        assertThat(time2).isEqualTo(0);
        assertThat(time3).isEqualTo(0);
        assertThat(time4).isEqualTo(0);
        assertThat(time5).isEqualTo(0);
        assertThat(time6).isEqualTo(0);
        assertThat(new DateTime(time7, UTC)).isEqualTo(new DateTime(2013, 1, 1, 12, 5, 17, 257, UTC));
        assertThat(time8).isEqualTo(0);
        assertThat(time9).isEqualTo(0);
        assertThat(time10).isEqualTo(0);
        assertThat(time11).isEqualTo(0);
        assertThat(time12).isEqualTo(0);
        assertThat(new DateTime(time13, UTC)).isEqualTo(new DateTime(2013, 1, 1, 13, 13, 25, 800, UTC));

    }
}

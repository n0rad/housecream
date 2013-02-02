package net.awired.housecream.camel.solar;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

/*

 2012-12-31T05:46:21.475-05:00
 2012-12-31T06:19:14.356-05:00
 2012-12-31T06:53:10.463-05:00
 2012-12-31T07:23:35.344-05:00
 2012-12-31T07:26:48.373-05:00
 2012-12-31T12:05:04.177-05:00
 2012-12-31T16:43:19.982-05:00
 2012-12-31T16:46:33.010-05:00
 2012-12-31T17:16:57.891-05:00
 2012-12-31T17:50:53.998-05:00
 2012-12-31T18:23:46.880-05:00


 */

public class SolarCalculatorTest {
    private static final DateTimeZone ZONE = DateTimeZone.forID("America/New_York");

    @Test
    public void should_find_noon() throws Exception {
        SolarAdvancedCalculator calculator = new SolarAdvancedCalculator(39.9522222, -75.1641667, ZONE.toTimeZone());
        DateTime date = new DateTime(2013, 1, 1, 0, 0, ZONE);

        SolarState findNextEvent = calculator.findNextEvent(date, SolarTimeEvent.values());
        System.out.println(findNextEvent);
    }
}

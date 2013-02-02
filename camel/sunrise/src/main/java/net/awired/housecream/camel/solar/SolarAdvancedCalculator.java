package net.awired.housecream.camel.solar;

import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class SolarAdvancedCalculator {

    private final double latitude;
    private final double longitude;
    private final DateTimeZone timezone;

    public SolarAdvancedCalculator(double latitude, double longitude, TimeZone timezone) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = DateTimeZone.forTimeZone(timezone);
    }

    //    public SolarState findCurrentState() {
    //        return findCurrentState(new DateTime(timezone));
    //    }
    //
    //    SolarState findCurrentState(DateTime date) {
    //        return null;
    //    }

    public SolarState findNextEvent() {
        return findNextEvent(new DateTime(timezone), SolarTimeEvent.values());
    }

    public SolarState findNextEvent(SolarTimeEvent[] seekingEvents) {
        return findNextEvent(new DateTime(timezone), seekingEvents);
    }

    ////////////////////////////////////////////////////////////////////////////////////

    SolarState findNextEvent(DateTime date, SolarTimeEvent[] seekingEvents) {
        DateTime nearestDate = null;
        SolarTimeEvent nearestEvent = null;
        for (SolarTimeEvent solarTimeEvent : seekingEvents) {
            Long time = SolarCalculator.getTime(solarTimeEvent, date.getMillis(), latitude, longitude);
            DateTime dateTime = new DateTime(time, timezone);
            nearestDate = dateTime;
            nearestEvent = solarTimeEvent;
            break;
        }
        return buildState(nearestDate, nearestEvent);
    }

    private SolarState buildState(DateTime date, SolarTimeEvent event) {
        return new SolarState(latitude, longitude, date, event);
    }

    ////////////////////////////////////////////////////////////////////////////////////

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public TimeZone getTimezone() {
        return timezone.toTimeZone();
    }

}

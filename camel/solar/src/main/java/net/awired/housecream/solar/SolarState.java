package net.awired.housecream.solar;

import net.awired.housecream.solar.enumeration.SolarTimeEvent;
import net.awired.housecream.solar.enumeration.SolarTimePhase;
import org.joda.time.DateTime;
import com.google.common.base.Objects;

public class SolarState {
    private final SolarTimeEvent event;
    private final SolarTimePhase phase;
    private final DateTime date;
    private final double latitude;
    private final double longitude;

    //    private final TimeZone timezone;

    public SolarState(double latitude, double longitude, DateTime date, SolarTimePhase phase) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.phase = phase;
        this.event = null;
    }

    public SolarState(double latitude, double longitude, DateTime date, SolarTimeEvent event) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.event = event;
        this.phase = event.getPhaseStarting();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass()) //
                .add("latitude", latitude) //
                .add("longitude", longitude) //
                .add("date", date) //
                .add("phase", phase) //
                .add("event", event) //
                .toString();
    }

    public SolarTimeEvent getEvent() {
        return event;
    }

    public SolarTimePhase getPhase() {
        return phase;
    }

    public DateTime getDate() {
        return date;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}

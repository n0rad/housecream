/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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

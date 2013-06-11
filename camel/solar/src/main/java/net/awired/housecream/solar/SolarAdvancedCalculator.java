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

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import net.awired.housecream.solar.enumeration.SolarTimeEvent;
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

    public SolarState findCurrentState() {
        return findCurrentState(new DateTime(timezone));
    }

    public SolarState findNextEvent() {
        return findNextEvent(new DateTime(timezone), Arrays.asList(SolarTimeEvent.values()));
    }

    public SolarState findNextEvent(List<SolarTimeEvent> seekingEvents) {
        return findNextEvent(new DateTime(timezone), seekingEvents);
    }

    ////////////////////////////////////////////////////////////////////////////////////

    SolarState findCurrentState(DateTime date) {
        SolarState nextEvent = findNextEvent(date, null);
        return new SolarState(latitude, longitude, date, nextEvent.getEvent().findPrevious().getPhaseStarting());
    }

    SolarState findNextEvent(DateTime date, List<SolarTimeEvent> seekingEvents) {
        List<SolarTimeEvent> processingEvents = seekingEvents;
        if (processingEvents == null || seekingEvents.isEmpty()) {
            processingEvents = Arrays.asList(SolarTimeEvent.values());
        }
        DateTime nearestDate = null;
        SolarTimeEvent nearestEvent = null;
        DateTime processingDate = date;
        do {
            for (SolarTimeEvent solarTimeEvent : processingEvents) {
                long time = SolarCalculator.getTime(solarTimeEvent, processingDate.getMillis(), latitude, longitude);
                if (date.isAfter(time)) {
                    continue; // skip past event 
                }
                DateTime dateTime = new DateTime(time, DateTimeZone.UTC).toDateTime(timezone);
                if (nearestDate == null || dateTime.isBefore(nearestDate)) { // override if closest event
                    nearestDate = dateTime;
                    nearestEvent = solarTimeEvent;
                }
            }
            processingDate = processingDate.plusDays(1);
        } while (nearestDate == null);

        return new SolarState(latitude, longitude, nearestDate, nearestEvent);
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

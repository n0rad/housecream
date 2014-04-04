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

import org.housecream.plugins.solar.enumeration.SolarTimeEvent;
import org.housecream.plugins.solar.enumeration.SolarTimePhase;
import org.joda.time.DateTime;
import lombok.Data;

@Data
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

}

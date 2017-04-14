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
package org.housecream.solar.enumeration;

public enum SolarTimePhase {

    ASTRONOMICAL_DAWN_TWILIGHT(SolarTimePhaseTwilight.DAWN_TWILIGHT, SolarTwilight.astronomical), // 
    NAUTICAL_DAWN_TWILIGHT(SolarTimePhaseTwilight.DAWN_TWILIGHT, SolarTwilight.nautical), // 
    CIVIL_DAWN_TWILIGHT(SolarTimePhaseTwilight.DAWN_TWILIGHT, SolarTwilight.civil), //
    SUNRISE(SolarTimePhaseTwilight.DAWN_TWILIGHT, SolarTwilight.official), //
    MORNING_GOLDER_HOUR(SolarTimePhaseTwilight.DAYTIME), //
    MORNING(SolarTimePhaseTwilight.DAYTIME), //
    AFTERNOON(SolarTimePhaseTwilight.DAYTIME), //
    AFTERNOON_GOLDER_HOUR(SolarTimePhaseTwilight.DAYTIME), //
    SUNSET(SolarTimePhaseTwilight.DUSK_TWILIGHT, SolarTwilight.official), //
    CIVIL_DUSK_TWILIGHT(SolarTimePhaseTwilight.DUSK_TWILIGHT, SolarTwilight.civil), //
    NAUTICAL_DUSK_TWILIGHT(SolarTimePhaseTwilight.DUSK_TWILIGHT, SolarTwilight.nautical), //
    ASTRONOMICAL_DUSK_TWILIGHT(SolarTimePhaseTwilight.DUSK_TWILIGHT, SolarTwilight.astronomical), //
    EARLY_NIGHT(SolarTimePhaseTwilight.NIGHTTIME), //
    LATE_NIGHT(SolarTimePhaseTwilight.NIGHTTIME), //
    ;

    private final SolarTimePhaseTwilight timePhaseTwilight;
    private final SolarTwilight twilight;

    private SolarTimePhase(SolarTimePhaseTwilight timePhaseTwilight) {
        this.timePhaseTwilight = timePhaseTwilight;
        this.twilight = null;
    }

    private SolarTimePhase(SolarTimePhaseTwilight timePhaseTwilight, SolarTwilight twilight) {
        this.timePhaseTwilight = timePhaseTwilight;
        this.twilight = twilight;
    }

    public SolarTimePhaseTwilight getTimePhaseTwilight() {
        return timePhaseTwilight;
    }

    public SolarTwilight getTwilight() {
        return twilight;
    }

}

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
package org.housecream.plugins.solar.enumeration;

public enum SolarTimeEvent {
    nadir(-90, SolarTimePhase.LATE_NIGHT), //
    astronomicalDawn(-18, SolarTimePhase.ASTRONOMICAL_DAWN_TWILIGHT), //
    nauticalDawn(-12, SolarTimePhase.NAUTICAL_DAWN_TWILIGHT), //
    civilDawn(-6, SolarTimePhase.CIVIL_DAWN_TWILIGHT), //
    sunriseStart(-0.833, SolarTimePhase.SUNRISE), //
    sunriseEnd(-0.3, SolarTimePhase.MORNING_GOLDER_HOUR), //
    morningGoldenHourEnd(6, SolarTimePhase.MORNING), //
    noon(90, SolarTimePhase.AFTERNOON), //
    afternoonGoldenHourStart(6, SolarTimePhase.AFTERNOON_GOLDER_HOUR), //
    sunsetStart(-0.3, SolarTimePhase.SUNSET), //
    sunsetEnd(-0.833, SolarTimePhase.CIVIL_DUSK_TWILIGHT), //
    civilDusk(-6, SolarTimePhase.NAUTICAL_DUSK_TWILIGHT), //
    nauticalDusk(-12, SolarTimePhase.ASTRONOMICAL_DUSK_TWILIGHT), //
    astronomicalDusk(-18, SolarTimePhase.EARLY_NIGHT), //
    ;

    private final double degree;
    private final SolarTimePhase phaseStarting;

    private SolarTimeEvent(double degree, SolarTimePhase phaseStarting) {
        this.degree = degree;
        this.phaseStarting = phaseStarting;
    }

    public SolarTimeEvent findPrevious() {
        SolarTimeEvent previous = null;
        for (SolarTimeEvent current : values()) {
            if (current == this) {
                if (previous == null) {
                    return astronomicalDusk;
                }
                return previous;
            }
            previous = current;
        }
        return null;
    }

    //////////////////////////////////////////

    public double getDegree() {
        return degree;
    }

    public SolarTimePhase getPhaseStarting() {
        return phaseStarting;
    }

}

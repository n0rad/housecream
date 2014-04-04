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

import static org.housecream.plugins.solar.enumeration.SolarTimePhaseTwilight.DAWN_TWILIGHT;
import org.housecream.plugins.solar.enumeration.SolarTimeEvent;

public class SolarCalculator {

    private static final long OFFSET_2_MIN = 1000 * 60 * 2; // there is a offset error of 1 min 49 but I don't know why

    private static final double J_HALF_DAY = 0.5;
    private static final double J_DAY_DURATION = 1.0;
    private static final double RAD = Math.PI / 180;
    private static final double DAY_MS = 1000 * 60 * 60 * 24;
    private static final double J1970 = 2440588;
    private static final double J2000 = 2451545;
    private static final double M0 = RAD * 357.5291;
    private static final double M1 = RAD * 0.98560028;
    private static final double J0 = 0.0009;
    private static final double J1 = 0.0053;
    private static final double J2 = -0.0069;
    private static final double C1 = RAD * 1.9148;
    private static final double C2 = RAD * 0.0200;
    private static final double C3 = RAD * 0.0003;
    private static final double P = RAD * 102.9372;
    private static final double E = RAD * 23.45;
    private static final double TH0 = RAD * 280.1600;
    private static final double TH1 = RAD * 360.9856235;

    public static long getTime(SolarTimeEvent solarTime, long utcDate, double latitude, double longitude) {
        long workDate = utcDate + OFFSET_2_MIN;
        double lw = RAD * -longitude;
        double phi = RAD * latitude;
        double J = dateToJulianDate(workDate);
        double n = getJulianCycle(J, lw);
        double js = getApproxTransit(0, lw, n);
        double m = getSolarMeanAnomaly(js);
        double c = getEquationOfCenter(m);
        double ls = getEclipticLongitude(m, c);
        double d = getSunDeclination(ls);
        double jnoon = getSolarTransit(js, m, ls);

        if (solarTime == SolarTimeEvent.noon) {
            return julianDateToDate(jnoon);
        }
        if (solarTime == SolarTimeEvent.nadir) {
            return julianDateToDate(jnoon - (J_DAY_DURATION / 2)); // TODO this is not accurate, but I don't know how to find it
        }

        double Jset = getSetJ(solarTime.getDegree() * RAD, phi, d, lw, n, m, ls);
        if (solarTime.getPhaseStarting().getTimePhaseTwilight() == DAWN_TWILIGHT
                || solarTime == SolarTimeEvent.sunriseEnd || solarTime == SolarTimeEvent.morningGoldenHourEnd) {
            double Jrise = jnoon - (Jset - jnoon);
            return julianDateToDate(Jrise);
        }
        return julianDateToDate(Jset);
    }

    public static SolarPosition getPosition(long utcDate, double latitude, double longitude) {
        //        long workDate = utcDate + offset_2_min;
        double lw = RAD * -longitude;
        double phi = RAD * latitude;
        double j = dateToJulianDate(utcDate);
        double m = getSolarMeanAnomaly(j);
        double c = getEquationOfCenter(m);
        double ls = getEclipticLongitude(m, c);
        double d = getSunDeclination(ls);
        double a = getRightAscension(ls);
        double th = getSiderealTime(j, lw);
        double h = th - a;
        return new SolarPosition(getAzimuth(h, phi, d), getAltitude(h, phi, d));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    // date conversions
    private static double dateToJulianDate(long date) {
        return date / DAY_MS - J_HALF_DAY + J1970;
    }

    private static long julianDateToDate(double j) {
        return Math.round((j + J_HALF_DAY - J1970) * DAY_MS);
    }

    // general sun calculations
    private static double getJulianCycle(double j, double lw) {
        return Math.round(j - J2000 - J0 - lw / (2 * Math.PI));
    }

    private static double getSolarMeanAnomaly(double js) {
        return M0 + M1 * (js - J2000);
    }

    private static double getEquationOfCenter(double m) {
        return C1 * Math.sin(m) + C2 * Math.sin(2 * m) + C3 * Math.sin(3 * m);
    }

    private static double getEclipticLongitude(double m, double c) {
        return m + P + c + Math.PI;
    }

    private static double getSunDeclination(double ls) {
        return Math.asin(Math.sin(ls) * Math.sin(E));
    }

    // calculations for sun times
    private static double getApproxTransit(double ht, double lw, double n) {
        return J2000 + J0 + (ht + lw) / (2 * Math.PI) + n;
    }

    private static double getSolarTransit(double js, double M, double ls) {
        return js + (J1 * Math.sin(M)) + (J2 * Math.sin(2 * ls));
    }

    private static double getHourAngle(double h, double phi, double d) {
        return Math.acos((Math.sin(h) - Math.sin(phi) * Math.sin(d)) / (Math.cos(phi) * Math.cos(d)));
    }

    // calculations for sun position
    private static double getRightAscension(double ls) {
        return Math.atan2(Math.sin(ls) * Math.cos(E), Math.cos(ls));
    }

    private static double getSiderealTime(double j, double lw) {
        return TH0 + TH1 * (j - J2000) - lw;
    }

    private static double getAzimuth(double h, double phi, double d) {
        return Math.atan2(Math.sin(h), Math.cos(h) * Math.sin(phi) - Math.tan(d) * Math.cos(phi));
    }

    private static double getAltitude(double h, double phi, double d) {
        return Math.asin(Math.sin(phi) * Math.sin(d) + Math.cos(phi) * Math.cos(d) * Math.cos(h));
    }

    private static double getSetJ(double h, double phi, double d, double lw, double n, double m, double ls) {
        double w = getHourAngle(h, phi, d);
        double a = getApproxTransit(w, lw, n);
        return getSolarTransit(a, m, ls);
    }

}

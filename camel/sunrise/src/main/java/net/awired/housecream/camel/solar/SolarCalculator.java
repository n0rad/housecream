package net.awired.housecream.camel.solar;

import java.util.Date;

public class SolarCalculator {

    private static double rad = Math.PI / 180;
    private static double dayMs = 1000 * 60 * 60 * 24;
    private static double J1970 = 2440588;
    private static double J2000 = 2451545;
    private static double M0 = rad * 357.5291;
    private static double M1 = rad * 0.98560028;
    private static double J0 = 0.0009;
    private static double J1 = 0.0053;
    private static double J2 = -0.0069;
    private static double C1 = rad * 1.9148;
    private static double C2 = rad * 0.0200;
    private static double C3 = rad * 0.0003;
    private static double P = rad * 102.9372;
    private static double e = rad * 23.45;
    private static double th0 = rad * 280.1600;
    private static double th1 = rad * 360.9856235;

    public static Long getTime(SolarTimeEvent solarTime, long date, double latitude, double longitude) {
        double lw = rad * -longitude;
        double phi = rad * latitude;
        long J = dateToJulianDate(date);
        double n = getJulianCycle(J, lw);
        double Js = getApproxTransit(0, lw, n);
        double M = getSolarMeanAnomaly(Js);
        double C = getEquationOfCenter(M);
        double Ls = getEclipticLongitude(M, C);
        double d = getSunDeclination(Ls);
        double Jnoon = getSolarTransit(Js, M, Ls);

        if (solarTime == SolarTimeEvent.noon) {
            return julianDateToDate(Jnoon);
        }
        if (solarTime == SolarTimeEvent.nadir) {
            return julianDateToDate(Jnoon - (dayMs / 2)); // TODO that is not accurate, but I don't know how to find it
        }

        double Jset = getSetJ(solarTime.getDegree() * rad, phi, d, lw, n, M, Ls);
        if (solarTime.getPhaseStarting().getTimePhaseTwilight() == SolarTimePhaseTwilight.DAWN_TWILIGHT) {
            double Jrise = Jnoon - (Jset - Jnoon);
            return julianDateToDate(Jrise);
        }
        return julianDateToDate(Jset);
    }

    public static SolarPosition getPosition(long date, double lat, double lng) {
        double lw = rad * -lng;
        double phi = rad * lat;
        long J = dateToJulianDate(date);
        double M = getSolarMeanAnomaly(J);
        double C = getEquationOfCenter(M);
        double Ls = getEclipticLongitude(M, C);
        double d = getSunDeclination(Ls);
        double a = getRightAscension(Ls);
        double th = getSiderealTime(J, lw);
        double H = th - a;
        return new SolarPosition(getAzimuth(H, phi, d), getAltitude(H, phi, d));
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////

    // date conversions
    private static long dateToJulianDate(long date) {
        return (long) (date / dayMs - 0.5 + J1970);
    }

    private static long julianDateToDate(Double j) {
        return new Date((long) ((j + 0.5 - J1970) * dayMs)).getTime();
    }

    // general sun calculations
    private static double getJulianCycle(long J, double lw) {
        return Math.round(J - J2000 - J0 - lw / (2 * Math.PI));
    }

    private static double getSolarMeanAnomaly(double Js) {
        return M0 + M1 * (Js - J2000);
    }

    private static double getEquationOfCenter(double M) {
        return C1 * Math.sin(M) + C2 * Math.sin(2 * M) + C3 * Math.sin(3 * M);
    }

    private static double getEclipticLongitude(double M, double C) {
        return M + P + C + Math.PI;
    }

    private static double getSunDeclination(double Ls) {
        return Math.asin(Math.sin(Ls) * Math.sin(e));
    }

    // calculations for sun times
    private static double getApproxTransit(double Ht, double lw, double n) {
        return J2000 + J0 + (Ht + lw) / (2 * Math.PI) + n;
    }

    private static double getSolarTransit(double Js, double M, double Ls) {
        return Js + (J1 * Math.sin(M)) + (J2 * Math.sin(2 * Ls));
    }

    private static double getHourAngle(double h, double phi, double d) {
        return Math.acos((Math.sin(h) - Math.sin(phi) * Math.sin(d)) / (Math.cos(phi) * Math.cos(d)));
    }

    // calculations for sun position
    private static double getRightAscension(double Ls) {
        return Math.atan2(Math.sin(Ls) * Math.cos(e), Math.cos(Ls));
    }

    private static double getSiderealTime(long J, double lw) {
        return th0 + th1 * (J - J2000) - lw;
    }

    private static double getAzimuth(double H, double phi, double d) {
        return Math.atan2(Math.sin(H), Math.cos(H) * Math.sin(phi) - Math.tan(d) * Math.cos(phi));
    }

    private static double getAltitude(double H, double phi, double d) {
        return Math.asin(Math.sin(phi) * Math.sin(d) + Math.cos(phi) * Math.cos(d) * Math.cos(H));
    }

    private static double getSetJ(double h, double phi, double d, double lw, double n, double M, double Ls) {
        double w = getHourAngle(h, phi, d);
        double a = getApproxTransit(w, lw, n);
        return getSolarTransit(a, M, Ls);
    }

}

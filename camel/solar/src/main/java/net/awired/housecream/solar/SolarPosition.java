package net.awired.housecream.solar;

import com.google.common.base.Objects;

public class SolarPosition {
    private final double azimuth;
    private final double altitude;

    public SolarPosition(double azimuth, double altitude) {
        this.azimuth = azimuth;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this) //
                .add("azimuth", azimuth) //
                .add("altitude", altitude) //
                .toString();
    }

    public double getAzimuth() {
        return azimuth;
    }

    public double getAltitude() {
        return altitude;
    }

}

package net.awired.housecream.camel.solar;

public enum SolarTimeEvent {
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
    nadir(-90, SolarTimePhase.LATE_NIGHT), //
    ;

    private final double degree;
    private final SolarTimePhase phaseStarting;

    private SolarTimeEvent(double degree, SolarTimePhase phaseStarting) {
        this.degree = degree;
        this.phaseStarting = phaseStarting;
    }

    public double getDegree() {
        return degree;
    }

    public SolarTimePhase getPhaseStarting() {
        return phaseStarting;
    }

}

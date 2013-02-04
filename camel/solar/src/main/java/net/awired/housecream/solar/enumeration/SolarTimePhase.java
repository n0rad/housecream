package net.awired.housecream.solar.enumeration;

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

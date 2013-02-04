package net.awired.housecream.solar.enumeration;


public enum SolarTimePhaseTwilight {
    DAWN_TWILIGHT(SolarTimePhaseGlobal.NIGHTTIME), // 
    DAYTIME(SolarTimePhaseGlobal.DAYTIME), // 
    DUSK_TWILIGHT(SolarTimePhaseGlobal.NIGHTTIME), // 
    NIGHTTIME(SolarTimePhaseGlobal.NIGHTTIME); //

    private final SolarTimePhaseGlobal globalPhase;

    private SolarTimePhaseTwilight(SolarTimePhaseGlobal globalPhase) {
        this.globalPhase = globalPhase;
    }

    public SolarTimePhaseGlobal getGlobalPhase() {
        return globalPhase;
    }
}

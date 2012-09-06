package net.awired.housecream.server.it.builder;

import net.awired.housecream.server.it.restmcu.LatchPinResource.PinInfo;
import net.awired.restmcu.api.domain.pin.RestMcuPin;
import net.awired.restmcu.api.domain.pin.RestMcuPinSettings;

public class PinInfoBuilder {
    private String name;
    private float value;

    public PinInfo build() {
        PinInfo pinInfo = new PinInfo();
        pinInfo.description = new RestMcuPin();
        pinInfo.settings = new RestMcuPinSettings();
        pinInfo.value = value;
        pinInfo.settings.setName(name);
        return pinInfo;
    }

    public PinInfoBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PinInfoBuilder value(float value) {
        this.value = value;
        return this;
    }

}

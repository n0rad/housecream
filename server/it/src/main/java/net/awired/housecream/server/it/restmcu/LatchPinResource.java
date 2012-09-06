package net.awired.housecream.server.it.restmcu;

import java.util.HashMap;
import java.util.Map;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.pin.RestMcuPin;
import net.awired.restmcu.api.domain.pin.RestMcuPinSettings;
import net.awired.restmcu.api.resource.client.RestMcuPinResource;

public class LatchPinResource implements RestMcuPinResource {

    public static class PinInfo {
        public RestMcuPinSettings settings;
        public RestMcuPin description;
        public float value;
    }

    private Map<Integer, PinInfo> pins = new HashMap<Integer, LatchPinResource.PinInfo>();

    public void pin(int pinId, PinInfo pinInfo) {
        pins.put(pinId, pinInfo);
    }

    public PinInfo pin(int pinId) {
        return pins.get(pinId);
    }

    @Override
    public RestMcuPin getPin(Integer pinId) throws NotFoundException {
        PinInfo pinInfo = pins.get(pinId);
        if (pinInfo == null) {
            throw new NotFoundException("pin not found" + pinId);
        }
        return pinInfo.description;
    }

    @Override
    public RestMcuPinSettings getPinSettings(Integer pinId) throws NotFoundException, UpdateException {
        PinInfo pinInfo = pins.get(pinId);
        if (pinInfo == null) {
            throw new NotFoundException("pin not found" + pinId);
        }
        return pinInfo.settings;
    }

    @Override
    public void setPinSettings(Integer pinId, RestMcuPinSettings pinSettings) throws NotFoundException,
            UpdateException {
        PinInfo pinInfo = pins.get(pinId);
        if (pinInfo == null) {
            throw new NotFoundException("pin not found" + pinId);
        }

        if (pinSettings.getName() != null) {
            pinInfo.settings.setName(pinSettings.getName());
        }

        if (pinSettings.getNotifies() != null) {
            pinInfo.settings.setNotifies(pinSettings.getNotifies());
        }
    }

    @Override
    public Float getPinValue(Integer pinId) throws NotFoundException {
        PinInfo pinInfo = pins.get(pinId);
        if (pinInfo == null) {
            throw new NotFoundException("pin not found" + pinId);
        }
        return pinInfo.value;
    }

    @Override
    public void setPinValue(Integer pinId, Float value) throws NotFoundException, UpdateException {
        PinInfo pinInfo = pins.get(pinId);
        if (pinInfo == null) {
            throw new NotFoundException("pin not found" + pinId);
        }
        pinInfo.value = value;
    }

}

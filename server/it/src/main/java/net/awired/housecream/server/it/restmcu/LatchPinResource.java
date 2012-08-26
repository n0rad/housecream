package net.awired.housecream.server.it.restmcu;

import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.restmcu.api.domain.pin.RestMcuPin;
import net.awired.restmcu.api.domain.pin.RestMcuPinSettings;
import net.awired.restmcu.api.resource.client.RestMcuPinResource;

public class LatchPinResource implements RestMcuPinResource {

    @Override
    public RestMcuPin getPin(Integer pinId) throws NotFoundException {
        return null;
    }

    @Override
    public RestMcuPinSettings getPinSettings(Integer pinId) throws NotFoundException, UpdateException {
        return null;
    }

    @Override
    public void setPinSettings(Integer pinId, RestMcuPinSettings pinSettings) throws NotFoundException,
            UpdateException {

    }

    @Override
    public Float getPinValue(Integer pinId) throws NotFoundException {
        return null;
    }

    @Override
    public void setPinValue(Integer pinId, Float value) throws NotFoundException, UpdateException {
    }

}

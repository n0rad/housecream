package net.awired.housecream.client.stub;

import net.awired.housecream.client.common.domain.pin.HccPin;
import net.awired.housecream.client.common.resource.HccPinNotFoundException;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.resource.client.HccPinResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PinResourceStub implements HccPinResource {

    @Autowired
    HccContext hccContext;

    @Override
    public HccPin getPin(int pinId) throws HccPinNotFoundException {
        return hccContext.getPin(pinId);
    }

    @Override
    public void setPin(int pinId, HccPin pin) throws HccPinNotFoundException, HccUpdateException {
        //        hccContext.updatePin(pinId, pin);
    }

    @Override
    public Float getPinValue(int pinId) throws HccPinNotFoundException {
        //        return hccContext.getPin(pinId).getValue();
        return null;
    }

    @Override
    public void setPinValue(int pinId, Float value) throws HccPinNotFoundException, HccUpdateException {
        //        hccContext.setPinValue(pinId, value);
    }

}

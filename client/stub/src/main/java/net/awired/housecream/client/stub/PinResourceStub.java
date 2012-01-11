package net.awired.housecream.client.stub;

import net.awired.housecream.client.common.domain.HccPinDescription;
import net.awired.housecream.client.common.domain.HccPinInfo;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.resource.PinNotFoundException;
import net.awired.housecream.client.common.resource.PinResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PinResourceStub implements PinResource {

    @Autowired
    HccContext hccContext;

    @Override
    public HccPinDescription getPinDescription(int pinId) throws PinNotFoundException {
        return hccContext.getPin(pinId).getDescription();
    }

    @Override
    public HccPinInfo getPinInfo(int pinId) throws PinNotFoundException {
        return hccContext.getPin(pinId).getInfo();
    }

    @Override
    public void setPinInfo(int pinId, HccPinInfo pin) throws PinNotFoundException, HccUpdateException {
        hccContext.updatePin(pinId, pin);
    }

    @Override
    public Integer getValue(int pinId) throws PinNotFoundException {
        return hccContext.getPin(pinId).getValue();
    }

    @Override
    public void setValue(int pinId, Integer value) throws PinNotFoundException, HccUpdateException {
        hccContext.setPinValue(pinId, value);
    }

}

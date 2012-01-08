package net.awired.housecream.client.stub;

import net.awired.housecream.client.common.domain.HccPin;
import net.awired.housecream.client.common.resource.PinNotFoundException;
import net.awired.housecream.client.common.resource.PinResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PinResourceStub implements PinResource {

    @Autowired
    HccContext hccContext;

    @Override
    public HccPin getPin(int pinId) throws PinNotFoundException {
        return hccContext.getPin(pinId);
    }

}

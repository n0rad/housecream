package net.awired.housecream.client.stub;

import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.resource.HccResource;
import net.awired.housecream.client.common.resource.HccUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HccResourceStub implements HccResource {

    @Autowired
    private HccContext context;

    @Override
    public HccDevice getDeviceInfo() {
        return context.getDevice();
    }

    @Override
    public HccDevice updateDevice(HccDevice deviceInfo) throws HccUpdateException {
        context.updateDevice(deviceInfo);
        return context.getDevice();
    }

}

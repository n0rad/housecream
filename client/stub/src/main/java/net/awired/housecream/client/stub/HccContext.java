package net.awired.housecream.client.stub;

import java.util.ArrayList;
import java.util.List;
import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.domain.HccPin;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.resource.PinNotFoundException;
import net.awired.housecream.client.common.test.DefaultStubDomainHelper;
import org.springframework.stereotype.Component;

@Component
public class HccContext {

    private HccDevice device;

    private List<HccPin> pins = new ArrayList<HccPin>();

    public HccContext() {
        device = DefaultStubDomainHelper.buildDefaultDevice();
        for (int i = 0; i < device.getNumberOfPin(); i++) {
            pins.add(DefaultStubDomainHelper.buildDefaultPin(i));
        }
    }

    public HccDevice getDevice() {
        return device;
    }

    public HccPin getPin(int pinId) throws PinNotFoundException {
        if (pinId < 0 || pinId > pins.size() - 1) {
            throw new PinNotFoundException("cannot found pin with id " + pinId);
        }
        return pins.get(pinId);
    }

    //
    //    public void setDevice(HccDevice device) {
    //        this.device = device;
    //    }

    public void updateDevice(HccDevice newDevice) throws HccUpdateException {
        if (!device.getVersion().equals(newDevice.getVersion())) {
            throw new HccUpdateException("version is not updatable");
        }
        if (!device.getSoftware().equals(newDevice.getSoftware())) {
            throw new HccUpdateException("software is not updatable");
        }
        if (!device.getIp().equals(newDevice.getIp())) {
            throw new HccUpdateException("ip is not updatable");
        }
        if (!device.getPort().equals(newDevice.getPort())) {
            throw new HccUpdateException("port is not updatable");
        }
        if (!device.getMac().equals(newDevice.getMac())) {
            throw new HccUpdateException("mac is not updatable");
        }
        if (!device.getNumberOfPin().equals(newDevice.getNumberOfPin())) {
            throw new HccUpdateException("mac is not updatable");
        }
        this.device = newDevice;
    }

}

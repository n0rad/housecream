package net.awired.housecream.client.stub;

import java.util.HashMap;
import java.util.Map;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.domain.HccPin;
import net.awired.housecream.client.common.domain.HccPinDirection;
import net.awired.housecream.client.common.domain.HccPinInfo;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.resource.PinNotFoundException;
import net.awired.housecream.client.common.test.DefaultStubDomainHelper;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;

@Component
public class HccContext {

    private HccDevice device;

    Map<Integer, HccPin> pins = new HashMap<Integer, HccPin>();

    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public HccContext() {
        device = DefaultStubDomainHelper.buildDefaultDevice();
        for (int i = 0; i < device.getNumberOfPin(); i++) {
            pins.put(i, DefaultStubDomainHelper.buildDefaultPin(i));
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

    public void updateDevice(HccDevice newDevice) throws HccUpdateException {
        if (Strings.isNullOrEmpty(newDevice.getName())) {
            throw new HccUpdateException("name cannot be empty");
        }
        if (Strings.isNullOrEmpty(newDevice.getDescription())) {
            throw new HccUpdateException("description cannot be empty");
        }
        if (!device.getTechnicalDescription().equals(newDevice.getTechnicalDescription())) {
            throw new HccUpdateException("technical description is not updatable");
        }
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

    public void updatePin(int pinId, HccPinInfo pinInfo) throws HccUpdateException {
        if (pins.get(pinId).getDescription().getDirection() == HccPinDirection.RESERVED
                || pins.get(pinId).getDescription().getDirection() == HccPinDirection.NOTUSED) {
            throw new HccUpdateException("pin is not updatable");
        }
        //
        //        Validator v = validatorFactory.getValidator();
        //        Set<ConstraintViolation<HccPinDescription>> violation = v.validate(pin);
        //        if (violation.size() > 0) {
        //            throw new HccUpdateException(violation.iterator().next().getMessage());
        //        }
        this.pins.get(pinId).setInfo(pinInfo);
    }

    public void setPinValue(int pinId, int value) throws HccUpdateException {
        if (pins.get(pinId).getDescription().getDirection() == HccPinDirection.RESERVED
                || pins.get(pinId).getDescription().getDirection() == HccPinDirection.NOTUSED) {
            throw new HccUpdateException("pin is not updatable");
        }
        this.pins.get(pinId).setValue(value);
    }

}

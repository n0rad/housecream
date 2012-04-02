package net.awired.housecream.client.stub;

import java.util.HashMap;
import java.util.Map;
import net.awired.housecream.client.common.domain.HccCondition;
import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.domain.HccNotify;
import net.awired.housecream.client.common.domain.HccPin;
import net.awired.housecream.client.common.domain.HccPinDescription;
import net.awired.housecream.client.common.domain.HccPinDirection;
import net.awired.housecream.client.common.domain.HccPinInfo;
import net.awired.housecream.client.common.domain.HccPinNotify;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.resource.PinNotFoundException;
import net.awired.housecream.client.common.resource.server.HccNotifyResource;
import net.awired.housecream.client.common.test.DefaultStubDomainHelper;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;

@Component
public class HccContext {

    private HccDevice device;

    Map<Integer, HccPin> pins = new HashMap<Integer, HccPin>();

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

    public void updatePin(int pinId, HccPinInfo pinInfo) throws HccUpdateException, PinNotFoundException {
        HccPin hccPin = pins.get(pinId);
        if (hccPin == null) {
            throw new PinNotFoundException("cannot found pin with id " + pinId);
        }
        HccPinDescription description = hccPin.getDescription();
        if (description.getDirection() == HccPinDirection.RESERVED
                || description.getDirection() == HccPinDirection.NOTUSED) {
            throw new HccUpdateException("pin is not updatable");
        }

        if (Strings.isNullOrEmpty(pinInfo.getName())) {
            throw new HccUpdateException("name cannot be empty");
        }
        if (Strings.isNullOrEmpty(pinInfo.getDescription())) {
            throw new HccUpdateException("description cannot be empty");
        }

        //

        if (description.getDirection() == HccPinDirection.OUTPUT) {
            if (pinInfo.getStartVal() == null) {
                throw new HccUpdateException("start value cannot be null");
            }
            if (pinInfo.getNotifies() != null) {
                throw new HccUpdateException("cannot set notify on output pin");
            }
            if (pinInfo.getStartVal() > description.getValueMax()) {
                throw new HccUpdateException("cannot start with value : " + pinInfo.getStartVal()
                        + " superior than max value : " + description.getValueMax());
            }
            if (pinInfo.getStartVal() < description.getValueMin()) {
                throw new HccUpdateException("cannot start with value : " + pinInfo.getStartVal()
                        + " inferior than min value : " + description.getValueMin());
            }
            if (pinInfo.getStartVal() % description.getValueStep() != 0) {
                throw new HccUpdateException("cannot set start value : " + pinInfo.getStartVal()
                        + " that is not in step of " + description.getValueStep());
            }
        }
        if (description.getDirection() == HccPinDirection.INPUT) {
            if (pinInfo.getStartVal() != null) {
                throw new HccUpdateException("start value cannot be set for input pin");
            }

            if (pinInfo.getNotifies() != null) {
                for (HccNotify notify : pinInfo.getNotifies()) {
                    if (notify.getNotifyValue() > description.getValueMax()) {
                        throw new HccUpdateException("cannot notify for value : " + notify.getNotifyValue()
                                + " superior than max value : " + description.getValueMax());
                    }
                    if (notify.getNotifyValue() < description.getValueMin()) {
                        throw new HccUpdateException("cannot notify for value : " + notify.getNotifyValue()
                                + " inferior than min value : " + description.getValueMin());
                    }
                }
            }
        }

        this.pins.get(pinId).setInfo(pinInfo);
    }

    public void setPinValue(int pinId, Float value) throws HccUpdateException, PinNotFoundException {
        HccPin hccPin = pins.get(pinId);
        if (hccPin == null) {
            throw new PinNotFoundException("cannot found pin with id " + pinId);
        }
        HccPinDescription description = hccPin.getDescription();
        HccPinInfo pinInfo = hccPin.getInfo();
        if (description.getDirection() == HccPinDirection.RESERVED
                || description.getDirection() == HccPinDirection.NOTUSED) {
            throw new HccUpdateException("pin is not updatable");
        }
        if (description.getDirection() == HccPinDirection.INPUT) {
            throw new HccUpdateException("cannot set value on an input pin");
        }

        if (value > description.getValueMax()) {
            throw new HccUpdateException("cannot set value : " + value + " superior than max value : "
                    + description.getValueMax());
        }
        if (value < description.getValueMin()) {
            throw new HccUpdateException("cannot set value : " + value + " inferior than min value : "
                    + description.getValueMin());
        }
        if (value % description.getValueStep() != 0) {
            throw new HccUpdateException("cannot set value : " + value + " that is not in step of "
                    + description.getValueStep());
        }

        hccPin.setValue(value);
    }

    public void setdebugValue(int pinId, Float value) {
        HccPin hccPin = this.pins.get(pinId);
        Float currentValue = hccPin.getValue();
        hccPin.setValue(value);
        if (!currentValue.equals(value) && hccPin.getInfo().getNotifies() != null) {
            for (HccNotify notify : hccPin.getInfo().getNotifies()) {
                if (notify.getNotifyCondition() == HccCondition.inf_or_equal) {
                    if (currentValue > notify.getNotifyValue() && value <= notify.getNotifyValue()) {
                        notifyPinModification(pinId, notify, value);
                    }
                } else if (notify.getNotifyCondition() == HccCondition.sup_or_equal) {
                    if (currentValue < notify.getNotifyValue() && value >= notify.getNotifyValue()) {
                        notifyPinModification(pinId, notify, value);
                    }
                }
            }
        }

    }

    private void notifyPinModification(int pinId, HccNotify notify, Float value) {
        HccPinNotify pinNotify = new HccPinNotify();
        pinNotify.setCondition(notify);
        pinNotify.setPinId(pinId);
        pinNotify.setValue(value);
        pinNotify.setAddress(device.getIp());

        HccNotifyResource notifyResource = JAXRSClientFactory.create(device.getNotifyUrl(), HccNotifyResource.class);
        notifyResource.notify(pinNotify);
    }
}

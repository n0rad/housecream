package net.awired.housecream.client.stub;

import java.util.HashMap;
import java.util.Map;
import net.awired.housecream.client.common.domain.board.HccBoard;
import net.awired.housecream.client.common.domain.pin.HccPin;
import net.awired.housecream.client.common.resource.HccPinNotFoundException;
import net.awired.housecream.client.common.resource.HccUpdateException;
import net.awired.housecream.client.common.test.DefaultStubDomainHelper;
import org.springframework.stereotype.Component;
import com.google.common.base.Strings;

@Component
public class HccContext {

    private HccBoard board;

    Map<Integer, HccPin> pins = new HashMap<Integer, HccPin>();

    public HccContext() {
        board = DefaultStubDomainHelper.buildDefaultDevice();
        //        for (int i = 0; i < board.getNumberOfPin(); i++) {
        //            pins.put(i, DefaultStubDomainHelper.buildDefaultPin(i));
        //        }
    }

    public HccBoard getBoard() {
        return board;
    }

    public HccPin getPin(int pinId) throws HccPinNotFoundException {
        if (pinId < 0 || pinId > pins.size() - 1) {
            throw new HccPinNotFoundException("cannot found pin with id " + pinId);
        }
        return pins.get(pinId);
    }

    public void updateDevice(HccBoard board) throws HccUpdateException {
        if (Strings.isNullOrEmpty(board.getName())) {
            throw new HccUpdateException("name cannot be empty");
        }
        if (Strings.isNullOrEmpty(board.getDescription())) {
            throw new HccUpdateException("description cannot be empty");
        }
        if (!board.getDescription().equals(board.getDescription())) {
            throw new HccUpdateException("technical description is not updatable");
        }
        if (!board.getVersion().equals(board.getVersion())) {
            throw new HccUpdateException("version is not updatable");
        }
        if (!board.getSoftware().equals(board.getSoftware())) {
            throw new HccUpdateException("software is not updatable");
        }
        if (!board.getIp().equals(board.getIp())) {
            throw new HccUpdateException("ip is not updatable");
        }
        if (!board.getPort().equals(board.getPort())) {
            throw new HccUpdateException("port is not updatable");
        }
        if (!board.getMac().equals(board.getMac())) {
            throw new HccUpdateException("mac is not updatable");
        }
        //        if (!board.getNumberOfPin().equals(board.getNumberOfPin())) {
        //            throw new HccUpdateException("mac is not updatable");
        //        }
        this.board = board;
    }

    //    public void updatePin(int pinId, HccPinInfo pinInfo) throws HccUpdateException, PinNotFoundException {
    //        HccPin hccPin = pins.get(pinId);
    //        if (hccPin == null) {
    //            throw new PinNotFoundException("cannot found pin with id " + pinId);
    //        }
    //        HccPin description = hccPin.getDescription();
    //        if (description.getDirection() == HccPinDirection.RESERVED
    //                || description.getDirection() == HccPinDirection.NOTUSED) {
    //            throw new HccUpdateException("pin is not updatable");
    //        }
    //
    //        if (Strings.isNullOrEmpty(pinInfo.getName())) {
    //            throw new HccUpdateException("name cannot be empty");
    //        }
    //        if (Strings.isNullOrEmpty(pinInfo.getDescription())) {
    //            throw new HccUpdateException("description cannot be empty");
    //        }
    //
    //        //
    //
    //        if (description.getDirection() == HccPinDirection.OUTPUT) {
    //            if (pinInfo.getStartVal() == null) {
    //                throw new HccUpdateException("start value cannot be null");
    //            }
    //            if (pinInfo.getNotifies() != null) {
    //                throw new HccUpdateException("cannot set notify on output pin");
    //            }
    //            if (pinInfo.getStartVal() > description.getValueMax()) {
    //                throw new HccUpdateException("cannot start with value : " + pinInfo.getStartVal()
    //                        + " superior than max value : " + description.getValueMax());
    //            }
    //            if (pinInfo.getStartVal() < description.getValueMin()) {
    //                throw new HccUpdateException("cannot start with value : " + pinInfo.getStartVal()
    //                        + " inferior than min value : " + description.getValueMin());
    //            }
    //            if (pinInfo.getStartVal() % description.getValueStep() != 0) {
    //                throw new HccUpdateException("cannot set start value : " + pinInfo.getStartVal()
    //                        + " that is not in step of " + description.getValueStep());
    //            }
    //        }
    //        if (description.getDirection() == HccPinDirection.INPUT) {
    //            if (pinInfo.getStartVal() != null) {
    //                throw new HccUpdateException("start value cannot be set for input pin");
    //            }
    //
    //            if (pinInfo.getNotifies() != null) {
    //                for (HccNotify notify : pinInfo.getNotifies()) {
    //                    if (notify.getNotifyValue() > description.getValueMax()) {
    //                        throw new HccUpdateException("cannot notify for value : " + notify.getNotifyValue()
    //                                + " superior than max value : " + description.getValueMax());
    //                    }
    //                    if (notify.getNotifyValue() < description.getValueMin()) {
    //                        throw new HccUpdateException("cannot notify for value : " + notify.getNotifyValue()
    //                                + " inferior than min value : " + description.getValueMin());
    //                    }
    //                }
    //            }
    //        }
    //
    //        this.pins.get(pinId).setInfo(pinInfo);
    //    }
    //
    //    public void setPinValue(int pinId, Float value) throws HccUpdateException, HccPinNotFoundException {
    //        HccPin hccPin = pins.get(pinId);
    //        if (hccPin == null) {
    //            throw new HccPinNotFoundException("cannot found pin with id " + pinId);
    //        }
    //        if (description.getDirection() == HccPinDirection.INPUT) {
    //            throw new HccUpdateException("cannot set value on an input pin");
    //        }
    //
    //        if (value > description.getValueMax()) {
    //            throw new HccUpdateException("cannot set value : " + value + " superior than max value : "
    //                    + description.getValueMax());
    //        }
    //        if (value < description.getValueMin()) {
    //            throw new HccUpdateException("cannot set value : " + value + " inferior than min value : "
    //                    + description.getValueMin());
    //        }
    //        if (value % description.getValueStep() != 0) {
    //            throw new HccUpdateException("cannot set value : " + value + " that is not in step of "
    //                    + description.getValueStep());
    //        }
    //
    //        hccPin.setValue(value);
    //    }
    //
    //    public void setdebugValue(int pinId, Float value) {
    //        HccPin hccPin = this.pins.get(pinId);
    //        Float currentValue = hccPin.getValue();
    //        hccPin.setValue(value);
    //        if (!currentValue.equals(value) && hccPin.getInfo().getNotifies() != null) {
    //            for (HccNotify notify : hccPin.getInfo().getNotifies()) {
    //                if (notify.getNotifyCondition() == HccCondition.inf_or_equal) {
    //                    if (currentValue > notify.getNotifyValue() && value <= notify.getNotifyValue()) {
    //                        notifyPinModification(pinId, notify, value);
    //                    }
    //                } else if (notify.getNotifyCondition() == HccCondition.sup_or_equal) {
    //                    if (currentValue < notify.getNotifyValue() && value >= notify.getNotifyValue()) {
    //                        notifyPinModification(pinId, notify, value);
    //                    }
    //                }
    //            }
    //        }
    //
    //    }
    //
    //    private void notifyPinModification(int pinId, HccNotify notify, Float value) {
    //        HccPinNotify pinNotify = new HccPinNotify();
    //        pinNotify.setCondition(notify);
    //        pinNotify.setPinId(pinId);
    //        pinNotify.setValue(value);
    //        pinNotify.setAddress(board.getIp());
    //
    //        HccNotifyResource notifyResource = JAXRSClientFactory.create(board.getNotifyUrl(), HccNotifyResource.class);
    //        notifyResource.notify(pinNotify);
    //    }
}

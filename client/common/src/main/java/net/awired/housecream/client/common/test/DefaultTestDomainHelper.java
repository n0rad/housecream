package net.awired.housecream.client.common.test;

import net.awired.housecream.client.common.domain.board.HccBoard;
import net.awired.housecream.client.common.domain.pin.HccPin;
import net.awired.housecream.client.common.domain.pin.HccPinDirection;
import net.awired.housecream.client.common.domain.pin.HccPinNotify;
import net.awired.housecream.client.common.domain.pin.HccPinNotifyCondition;
import net.awired.housecream.client.common.domain.pin.HccPinType;

class DefaultTestDomainHelper {

    public static HccPin buildDefaultPin(int num) {
        switch (num) {
            case 2: {
                HccPin pin = new HccPin();
                pin.setDescription("technical desc");
                pin.setDirection(HccPinDirection.INPUT);
                pin.setType(HccPinType.ANALOG);
                pin.setValueMin(0f);
                pin.setValueMax(1023f);

                pin.setName("pin2");
                pin.setDescription("input analog pin");
                pin.addNotify(new HccPinNotify(HccPinNotifyCondition.inf_or_equal, 0));
                pin.addNotify(new HccPinNotify(HccPinNotifyCondition.inf_or_equal, 42));
                //                pin.setValue(952f);
                return pin;
            }
            case 3: {
                HccPin pin = new HccPin();

                pin.setDescription("out technical desc");
                pin.setDirection(HccPinDirection.OUTPUT);
                pin.setType(HccPinType.ANALOG);
                pin.setValueMin(0f);
                pin.setValueMax(254f);
                //                pin.setValueStep(2f);

                pin.setName("pin3");
                pin.setDescription("output analog pin");
                //                pin.setValue(60f);
                return pin;
            }
            case 4: {
                HccPin pin = new HccPin();
                pin.setDescription("out technical desc42");
                pin.setDirection(HccPinDirection.OUTPUT);
                pin.setType(HccPinType.DIGITAL);
                pin.setValueMin(0f);
                pin.setValueMax(1f);

                pin.setName("pin4");
                pin.setDescription("output digital pin");
                //                pin.setValue(1f);
                return pin;
            }
            case 5: {
                HccPin pin = new HccPin();

                pin.setDescription("in technical desc43");
                pin.setDirection(HccPinDirection.INPUT);
                pin.setType(HccPinType.DIGITAL);
                pin.setValueMin(0f);
                pin.setValueMax(1f);

                pin.setName("pin5");
                pin.setDescription("input digital pin");
                pin.addNotify(new HccPinNotify(HccPinNotifyCondition.inf_or_equal, 0));
                pin.addNotify(new HccPinNotify(HccPinNotifyCondition.inf_or_equal, 1));
                //                pin.setValue(1f);
                return pin;
            }
            default:
                return null;
        }
    }

    public static HccBoard buildDefaultDevice() {
        HccBoard device = new HccBoard();
        device.setVersion("1.0");
        device.setSoftware("hcc");
        device.setNotifyUrl("http://localhost/4242");
        //        device.setNumberOfPin(6);
        device.setHardware("arduino");
        device.setName("sample board");
        device.setDescription("genre");
        device.setDescription("little description");
        device.setIp("123.456.789.123");
        device.setPort(8080);
        device.setMac("df:df:df:df:df:df");
        return device;
    }

}

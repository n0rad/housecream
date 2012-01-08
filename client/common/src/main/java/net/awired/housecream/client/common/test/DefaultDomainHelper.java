package net.awired.housecream.client.common.test;

import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.domain.HccPin;
import net.awired.housecream.client.common.domain.HccPinDirection;

class DefaultDomainHelper {

    public static HccPin buildDefaultPin(int num) {
        switch (num) {
            case 0: {
                HccPin pin = new HccPin();
                pin.setName("pin0");
                pin.setDescription("reserved pin");
                //                pin.setType(HccPinType.analog);
                pin.setDirection(HccPinDirection.RESERVED);
                return pin;
            }
                //            case 1:
                //                HccPin pin = new HccPin();
                //                pin.setName("pin1");
                //                pin.setDescription("pin description");
                //                pin.setType(HccPinType.analog);
                //                pin.setStartVal(44);
                //                pin.setRangeMax(255);
                //                pin.setRangeMin(0);
                //                pin.setRangeStep(1);
                //                pin.setDirection(HccPinDirection.OUTPUT);
                //                return pin;
            default:
                return null;
        }
    }

    public static HccDevice buildDefaultDevice() {
        HccDevice device = new HccDevice();
        device.setVersion("1.0");
        device.setSoftware("hcc");
        device.setNotifyUrl("http://localhost/4242");
        device.setNumberOfPin(1);
        device.setName("sample board");
        device.setDescription("little description");
        device.setIp("123.456.789.123");
        device.setPort(8080);
        device.setMac("df:df:df:df:df:df");
        return device;
    }

}

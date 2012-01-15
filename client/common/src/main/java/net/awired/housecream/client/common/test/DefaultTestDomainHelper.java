package net.awired.housecream.client.common.test;

import net.awired.housecream.client.common.domain.HccCondition;
import net.awired.housecream.client.common.domain.HccDevice;
import net.awired.housecream.client.common.domain.HccNotify;
import net.awired.housecream.client.common.domain.HccPin;
import net.awired.housecream.client.common.domain.HccPinDescription;
import net.awired.housecream.client.common.domain.HccPinDirection;
import net.awired.housecream.client.common.domain.HccPinInfo;
import net.awired.housecream.client.common.domain.HccPinType;

class DefaultTestDomainHelper {

    public static HccPin buildDefaultPin(int num) {
        switch (num) {
            case 0: {
                HccPin pin = new HccPin();
                HccPinDescription pinDesc = new HccPinDescription();
                pin.setDescription(pinDesc);

                pinDesc.setTechnicalDescription("technical desc");
                pinDesc.setDirection(HccPinDirection.RESERVED);
                pinDesc.setType(HccPinType.ANALOG);

                return pin;
            }
            case 1: {
                HccPin pin = new HccPin();
                HccPinDescription pinDesc = new HccPinDescription();
                pin.setDescription(pinDesc);

                pinDesc.setTechnicalDescription("technical desc");
                pinDesc.setDirection(HccPinDirection.NOTUSED);
                pinDesc.setType(HccPinType.DIGITAL);
                return pin;
            }
            case 2: {
                HccPin pin = new HccPin();
                HccPinDescription pinDesc = new HccPinDescription();
                HccPinInfo pinConf = new HccPinInfo();
                pin.setDescription(pinDesc);
                pin.setInfo(pinConf);

                pinDesc.setTechnicalDescription("technical desc");
                pinDesc.setDirection(HccPinDirection.INPUT);
                pinDesc.setType(HccPinType.ANALOG);
                pinDesc.setPullUp(true);
                pinDesc.setValueMin(0f);
                pinDesc.setValueMax(1023f);

                pinConf.setName("pin2");
                pinConf.setDescription("input analog pin");
                pinConf.addNotify(new HccNotify(HccCondition.inf_or_equal, 0));
                pinConf.addNotify(new HccNotify(HccCondition.inf_or_equal, 42));
                pin.setValue(952f);
                return pin;
            }
            case 3: {
                HccPin pin = new HccPin();
                HccPinDescription pinDesc = new HccPinDescription();
                HccPinInfo pinConf = new HccPinInfo();
                pin.setDescription(pinDesc);
                pin.setInfo(pinConf);

                pinDesc.setTechnicalDescription("out technical desc");
                pinDesc.setDirection(HccPinDirection.OUTPUT);
                pinDesc.setType(HccPinType.ANALOG);
                pinDesc.setPullUp(true);
                pinDesc.setValueMin(0f);
                pinDesc.setValueMax(254f);
                pinDesc.setValueStep(2f);

                pinConf.setName("pin3");
                pinConf.setDescription("output analog pin");
                pinConf.setStartVal(50f);
                pin.setValue(60f);
                return pin;
            }
            case 4: {
                HccPin pin = new HccPin();
                HccPinDescription pinDesc = new HccPinDescription();
                HccPinInfo pinConf = new HccPinInfo();
                pin.setDescription(pinDesc);
                pin.setInfo(pinConf);

                pinDesc.setTechnicalDescription("out technical desc42");
                pinDesc.setDirection(HccPinDirection.OUTPUT);
                pinDesc.setType(HccPinType.DIGITAL);
                pinDesc.setPullUp(true);
                pinDesc.setValueMin(0f);
                pinDesc.setValueMax(1f);
                pinDesc.setValueStep(1f);

                pinConf.setName("pin4");
                pinConf.setDescription("output digital pin");
                pinConf.setStartVal(1f);
                pin.setValue(1f);
                return pin;
            }
            case 5: {
                HccPin pin = new HccPin();
                HccPinDescription pinDesc = new HccPinDescription();
                HccPinInfo pinConf = new HccPinInfo();
                pin.setDescription(pinDesc);
                pin.setInfo(pinConf);

                pinDesc.setTechnicalDescription("in technical desc43");
                pinDesc.setDirection(HccPinDirection.INPUT);
                pinDesc.setType(HccPinType.DIGITAL);
                pinDesc.setPullUp(true);
                pinDesc.setValueMin(0f);
                pinDesc.setValueMax(1f);

                pinConf.setName("pin5");
                pinConf.setDescription("input digital pin");
                pinConf.addNotify(new HccNotify(HccCondition.inf_or_equal, 0));
                pinConf.addNotify(new HccNotify(HccCondition.inf_or_equal, 1));
                pin.setValue(1f);
                return pin;
            }
            default:
                return null;
        }
    }

    public static HccDevice buildDefaultDevice() {
        HccDevice device = new HccDevice();
        device.setVersion("1.0");
        device.setSoftware("hcc");
        device.setNotifyUrl("http://localhost/4242");
        device.setNumberOfPin(6);
        device.setHardware("arduino");
        device.setName("sample board");
        device.setTechnicalDescription("genre");
        device.setDescription("little description");
        device.setIp("123.456.789.123");
        device.setPort(8080);
        device.setMac("df:df:df:df:df:df");
        return device;
    }

}

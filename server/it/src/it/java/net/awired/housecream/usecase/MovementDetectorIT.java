package net.awired.housecream.usecase;

import static org.junit.Assert.assertEquals;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.domain.inpoint.InPointType;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;
import net.awired.housecream.server.common.domain.outPoint.OutPointType;
import net.awired.housecream.server.it.HcsTestRule;
import net.awired.housecream.server.it.RestServerRule;
import net.awired.restmcu.api.domain.pin.RestMcuPin;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotify;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotifyCondition;
import net.awired.restmcu.api.resource.client.RestMcuPinResource;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class MovementDetectorIT {

    @Rule
    public HcsTestRule hcs = new HcsTestRule();

    public static Float outputValue;

    public static class OutputLightResource implements RestMcuPinResource {

        @Override
        public RestMcuPin getPin(int pinId) throws NotFoundException {
            return null;
        }

        @Override
        public void setPin(int pinId, RestMcuPin pin) throws NotFoundException, UpdateException {
        }

        @Override
        public Float getPinValue(int pinId) throws NotFoundException {
            return null;
        }

        @Override
        public void setPinValue(int pinId, Float value) throws NotFoundException, UpdateException {
            if (pinId == 3) {
                outputValue = value;
            }
        }
    }

    @ClassRule
    public static RestServerRule restMcuPin = new RestServerRule(5879, OutputLightResource.class);

    @Test
    public void should_turn_on_the_light_when_someone_is_detected() throws Exception {
        InPoint inPoint = new InPoint();
        inPoint.setType(InPointType.PIR);
        inPoint.setName("my pir1");
        inPoint.setUrl("restmcu://localhost:5879/pin/2");
        hcs.getInPointResource().createInPoint(inPoint);

        OutPoint outPoint = new OutPoint();
        outPoint.setName("my light1");
        outPoint.setType(OutPointType.LIGHT);
        outPoint.setUrl("restmcu://localhost:5879/pin/3");
        hcs.getOutPointResource().createOutPoint(outPoint);

        RestMcuPinNotification pinNotification = new RestMcuPinNotification();
        pinNotification.setPinId(2);
        pinNotification.setOldValue(0);
        pinNotification.setValue(1);
        pinNotification.setNotify(new RestMcuPinNotify(RestMcuPinNotifyCondition.sup_or_equal, 1));
        hcs.getNotifyResource().pinNotification(pinNotification);

        Thread.sleep(1000);

        assertEquals((Float) 1f, outputValue);
    }
}

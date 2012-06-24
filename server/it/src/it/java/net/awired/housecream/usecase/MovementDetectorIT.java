package net.awired.housecream.usecase;

import static org.junit.Assert.assertEquals;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.domain.inpoint.InPointType;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;
import net.awired.housecream.server.common.domain.outPoint.OutPointType;
import net.awired.housecream.server.common.domain.rule.Condition;
import net.awired.housecream.server.common.domain.rule.Consequence;
import net.awired.housecream.server.common.domain.rule.EventRule;
import net.awired.housecream.server.it.HcsTestRule;
import net.awired.housecream.server.it.RestServerRule;
import net.awired.housecream.server.it.restmcu.RestMcuEmptyBoardResource;
import net.awired.housecream.server.it.restmcu.RestMcuEmptyPinResource;
import net.awired.housecream.usecase.InPointCreationIT.SwitchResource;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotify;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotifyCondition;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class MovementDetectorIT {

    @Rule
    public HcsTestRule hcs = new HcsTestRule();

    public static CountDownLatch latch = new CountDownLatch(1);

    public static Float outputValue;

    private static RestMcuBoard board;

    @Before
    public void before() {
        board = new RestMcuBoard();
    }

    public static class OutputLightResource extends RestMcuEmptyPinResource {
        @Override
        public void setPinValue(Integer pinId, Float value) throws NotFoundException, UpdateException {
            if (pinId == 3) {
                outputValue = value;
                latch.countDown();
            }
        }
    }

    public static class SwitchBoardResource extends RestMcuEmptyBoardResource {
        @Override
        public RestMcuBoard getBoard() {
            return board;
        }

        @Override
        public void setBoard(RestMcuBoard board2) throws UpdateException {
            board = board2;
        }
    }

    @ClassRule
    public static RestServerRule restMcuPin = new RestServerRule(5879, SwitchResource.class,
            SwitchBoardResource.class);

    @Test
    public void should_turn_on_the_light_when_someone_is_detected() throws Exception {
        // inpoint
        InPoint inPoint = new InPoint();
        inPoint.setType(InPointType.PIR);
        inPoint.setName("my pir1");
        inPoint.setUrl("restmcu://127.0.0.1:5879/pin/2");
        Long inPointId = hcs.getInPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPoint();
        outPoint.setName("my light1");
        outPoint.setType(OutPointType.LIGHT);
        outPoint.setUrl("restmcu://127.0.0.1:5879/pin/3");
        Long outPointId = hcs.getOutPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        Condition condition = new Condition();
        condition.setPointId(inPointId);
        condition.setValue(1);
        rule.getConditions().add(condition);
        Consequence consequence = new Consequence();
        consequence.setOutPointId(outPointId);
        consequence.setValue(1);
        rule.getConsequences().add(consequence);
        hcs.getRuleResource().createRule(rule);

        RestMcuPinNotification pinNotification = new RestMcuPinNotification();
        pinNotification.setPinId(2);
        pinNotification.setOldValue(0);
        pinNotification.setValue(1);
        pinNotification.setSource("127.0.0.1:5879");
        pinNotification.setNotify(new RestMcuPinNotify(RestMcuPinNotifyCondition.sup_or_equal, 1));
        hcs.getNotifyResource().pinNotification(pinNotification);

        latch.await(10, TimeUnit.SECONDS);

        assertEquals((Float) 1f, outputValue);
    }
}

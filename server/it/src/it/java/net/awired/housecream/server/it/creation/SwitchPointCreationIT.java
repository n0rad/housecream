package net.awired.housecream.server.it.creation;

import static org.junit.Assert.assertEquals;
import java.util.concurrent.CountDownLatch;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.domain.inpoint.InPointType;
import net.awired.housecream.server.common.domain.rule.Condition;
import net.awired.housecream.server.common.domain.rule.ConditionType;
import net.awired.housecream.server.common.domain.rule.EventRule;
import net.awired.housecream.server.it.HcsTestRule;
import net.awired.housecream.server.it.RestServerRule;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.restmcu.RestMcuEmptyBoardResource;
import net.awired.housecream.server.it.restmcu.RestMcuEmptyPinResource;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.domain.pin.RestMcuPin;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotify;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotifyCondition;
import net.awired.restmcu.api.resource.test.NotifBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class SwitchPointCreationIT {

    @Rule
    public HcsTestRule hcs = new HcsTestRule();

    public static CountDownLatch latch = new CountDownLatch(1);
    public static Float outputValue = 0f;
    private static RestMcuBoard board;
    private static RestMcuPin pin;

    @Before
    public void before() {
        board = new RestMcuBoard();
    }

    public static class PinResource extends RestMcuEmptyPinResource {
        @Override
        public Float getPinValue(Integer pinId) throws NotFoundException {
            return outputValue;
        }

    }

    public static class BoardResource extends RestMcuEmptyBoardResource {
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
    public static RestServerRule restMcuPin = new RestServerRule(5879, PinResource.class, BoardResource.class);

    @Test
    public void should_update_restmcu_when_adding_rule() throws Exception {
        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1")
                .url("restmcu://127.0.0.1:5879/pin/2").build();
        Long inPointId = hcs.getInPointResource().createInPoint(inPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        hcs.getRuleResource().createRule(rule);

        RestMcuPinNotification pinNotif = new NotifBuilder().pinId(2).oldValue(0).value(1).source("127.0.0.1:5879")
                .notify(RestMcuPinNotifyCondition.sup_or_equal, 1).build();
        hcs.getNotifyResource().pinNotification(pinNotif);

        Thread.sleep(1000);

        assertEquals("http://localhost:8080/hcs/ws/", board.getNotifyUrl());
        assertEquals(2, pin.getNotifies().size());
        assertEquals(new RestMcuPinNotify(RestMcuPinNotifyCondition.inf_or_equal, 0), pin.getNotifies().get(0));
        assertEquals(new RestMcuPinNotify(RestMcuPinNotifyCondition.sup_or_equal, 1), pin.getNotifies().get(1));
        assertEquals("my pir1", pin.getName());
    }

}

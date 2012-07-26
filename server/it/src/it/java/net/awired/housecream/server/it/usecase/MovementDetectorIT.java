package net.awired.housecream.server.it.usecase;

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
import net.awired.housecream.server.common.domain.rule.ConditionType;
import net.awired.housecream.server.common.domain.rule.Consequence;
import net.awired.housecream.server.common.domain.rule.EventRule;
import net.awired.housecream.server.it.HcsTestRule;
import net.awired.housecream.server.it.RestServerRule;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.it.restmcu.RestMcuEmptyBoardResource;
import net.awired.housecream.server.it.restmcu.RestMcuEmptyPinResource;
import net.awired.restmcu.api.domain.board.RestMcuBoard;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotifyCondition;
import net.awired.restmcu.api.resource.test.NotifBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class MovementDetectorIT {

    @Rule
    public HcsTestRule hcs = new HcsTestRule();

    public static CountDownLatch latch = new CountDownLatch(1);

    public static Float outputValue = 0f;

    private static RestMcuBoard board;

    @Before
    public void before() {
        board = new RestMcuBoard();
    }

    public static class OutputLightResource extends RestMcuEmptyPinResource {
        @Override
        public Float getPinValue(Integer pinId) throws NotFoundException {
            return outputValue;
        }

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
    public static RestServerRule restMcuPin = new RestServerRule(5879, OutputLightResource.class,
            SwitchBoardResource.class);

    @Test
    public void should_turn_on_the_light_when_someone_is_detected() throws Exception {
        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1")
                .url("restmcu://127.0.0.1:5879/pin/2").build();
        Long inPointId = hcs.getInPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT)
                .url("restmcu://127.0.0.1:5879/pin/3").build();
        Long outPointId = hcs.getOutPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        rule.getConsequences().add(new Consequence(outPointId, 1));
        hcs.getRuleResource().createRule(rule);

        RestMcuPinNotification pinNotif = new NotifBuilder().pinId(2).oldValue(0).value(1).source("127.0.0.1:5879")
                .notify(RestMcuPinNotifyCondition.sup_or_equal, 1).build();
        hcs.getNotifyResource().pinNotification(pinNotif);

        latch.await(10, TimeUnit.SECONDS);

        assertEquals((Float) 1f, hcs.getInPointResource().getPointValue(inPointId));
        assertEquals((Float) 1f, hcs.getInPointResource().getPointValue(outPointId));
        assertEquals((Float) 1f, outputValue);
    }

    @Test
    @Ignore
    public void should_toggle_light_state() throws Exception {
        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1")
                .url("restmcu://127.0.0.1:5879/pin/2").build();
        Long inPointId = hcs.getInPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT)
                .url("restmcu://127.0.0.1:5879/pin/3").build();
        Long outPointId = hcs.getOutPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        rule.getConditions().add(new Condition(outPointId, 0, ConditionType.state));
        rule.getConsequences().add(new Consequence(outPointId, 1));
        hcs.getRuleResource().createRule(rule);

        // rule 2
        EventRule rule2 = new EventRule();
        rule2.setName("my first rule2");
        rule2.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        rule2.getConditions().add(new Condition(outPointId, 1, ConditionType.state));
        rule2.getConsequences().add(new Consequence(outPointId, 0));
        hcs.getRuleResource().createRule(rule2);

        //notif
        RestMcuPinNotification pinNotif = new NotifBuilder().pinId(2).oldValue(0).value(1).source("127.0.0.1:5879")
                .notify(RestMcuPinNotifyCondition.sup_or_equal, 1).build();
        hcs.getNotifyResource().pinNotification(pinNotif);

        latch.await(10, TimeUnit.SECONDS);
        latch = new CountDownLatch(1);

        assertEquals((Float) 1f, hcs.getInPointResource().getPointValue(inPointId));
        assertEquals((Float) 1f, hcs.getInPointResource().getPointValue(outPointId));
        assertEquals((Float) 1f, outputValue);

        hcs.getNotifyResource().pinNotification(
                new NotifBuilder().pinId(2).oldValue(1).value(0).source("127.0.0.1:5879")
                        .notify(RestMcuPinNotifyCondition.sup_or_equal, 1).build());

        Thread.sleep(500);

        assertEquals((Float) 0f, hcs.getInPointResource().getPointValue(inPointId));
        assertEquals((Float) 1f, hcs.getInPointResource().getPointValue(outPointId));
        assertEquals((Float) 1f, outputValue);

        hcs.getNotifyResource().pinNotification(
                new NotifBuilder().pinId(2).oldValue(1).value(1).source("127.0.0.1:5879")
                        .notify(RestMcuPinNotifyCondition.sup_or_equal, 1).build());

        latch.await(10, TimeUnit.SECONDS);

        assertEquals((Float) 1f, hcs.getInPointResource().getPointValue(inPointId));
        assertEquals((Float) 0f, hcs.getInPointResource().getPointValue(outPointId));
        assertEquals((Float) 0f, outputValue);

    }
}

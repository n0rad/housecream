package net.awired.housecream.server.it.usecase;

import static org.junit.Assert.fail;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.domain.inpoint.InPointType;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;
import net.awired.housecream.server.common.domain.outPoint.OutPointType;
import net.awired.housecream.server.common.domain.rule.Condition;
import net.awired.housecream.server.common.domain.rule.ConditionType;
import net.awired.housecream.server.common.domain.rule.Consequence;
import net.awired.housecream.server.common.domain.rule.EventRule;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.it.restmcu.LatchBoardResource;
import net.awired.housecream.server.it.restmcu.LatchPinResource;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotifyCondition;
import net.awired.restmcu.api.resource.test.NotifBuilder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class MovementDetectorIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", LatchBoardResource.class,
            LatchPinResource.class);

    @Test
    public void should_turn_on_the_light_when_someone_is_detected() throws Exception {
        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1")
                .url("restmcu://127.0.0.1:5879/pin/2").build();
        Long inPointId = hcs.inPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT)
                .url("restmcu://127.0.0.1:5879/pin/3").build();
        Long outPointId = hcs.outPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        rule.getConsequences().add(new Consequence(outPointId, 1));
        hcs.ruleResource().createRule(rule);

        RestMcuPinNotification pinNotif = new NotifBuilder().pinId(2).oldValue(0).value(1).source("127.0.0.1:5879")
                .notify(RestMcuPinNotifyCondition.SUP_OR_EQUAL, 1).build();
        hcs.notifyResource().pinNotification(pinNotif);

        //        latch.await(10, TimeUnit.SECONDS);
        //
        //        assertEquals((Float) 1f, hcs.inPointResource().getPointValue(inPointId));
        //        assertEquals((Float) 1f, hcs.inPointResource().getPointValue(outPointId));
        //        assertEquals((Float) 1f, outputValue);
        fail();
    }

    @Test
    @Ignore
    public void should_toggle_light_state() throws Exception {
        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1")
                .url("restmcu://127.0.0.1:5879/pin/2").build();
        Long inPointId = hcs.inPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT)
                .url("restmcu://127.0.0.1:5879/pin/3").build();
        Long outPointId = hcs.outPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        rule.getConditions().add(new Condition(outPointId, 0, ConditionType.state));
        rule.getConsequences().add(new Consequence(outPointId, 1));
        hcs.ruleResource().createRule(rule);

        // rule 2
        EventRule rule2 = new EventRule();
        rule2.setName("my first rule2");
        rule2.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        rule2.getConditions().add(new Condition(outPointId, 1, ConditionType.state));
        rule2.getConsequences().add(new Consequence(outPointId, 0));
        hcs.ruleResource().createRule(rule2);

        //notif
        RestMcuPinNotification pinNotif = new NotifBuilder().pinId(2).oldValue(0).value(1).source("127.0.0.1:5879")
                .notify(RestMcuPinNotifyCondition.SUP_OR_EQUAL, 1).build();
        hcs.notifyResource().pinNotification(pinNotif);

        //        latch.await(10, TimeUnit.SECONDS);
        //        latch = new CountDownLatch(1);
        //
        //        assertEquals((Float) 1f, hcs.inPointResource().getPointValue(inPointId));
        //        assertEquals((Float) 1f, hcs.inPointResource().getPointValue(outPointId));
        //        assertEquals((Float) 1f, outputValue);
        //
        //        hcs.notifyResource().pinNotification(
        //                new NotifBuilder().pinId(2).oldValue(1).value(0).source("127.0.0.1:5879")
        //                        .notify(RestMcuPinNotifyCondition.SUP_OR_EQUAL, 1).build());
        //
        //        Thread.sleep(500);
        //
        //        assertEquals((Float) 0f, hcs.inPointResource().getPointValue(inPointId));
        //        assertEquals((Float) 1f, hcs.inPointResource().getPointValue(outPointId));
        //        assertEquals((Float) 1f, outputValue);
        //
        //        hcs.notifyResource().pinNotification(
        //                new NotifBuilder().pinId(2).oldValue(1).value(1).source("127.0.0.1:5879")
        //                        .notify(RestMcuPinNotifyCondition.SUP_OR_EQUAL, 1).build());
        //
        //        latch.await(10, TimeUnit.SECONDS);
        //
        //        assertEquals((Float) 1f, hcs.inPointResource().getPointValue(inPointId));
        //        assertEquals((Float) 0f, hcs.inPointResource().getPointValue(outPointId));
        //        assertEquals((Float) 0f, outputValue);
        fail();

    }
}

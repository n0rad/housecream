package net.awired.housecream.server.it.creation;

import static org.junit.Assert.assertEquals;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.domain.inpoint.InPointType;
import net.awired.housecream.server.common.domain.rule.Condition;
import net.awired.housecream.server.common.domain.rule.ConditionType;
import net.awired.housecream.server.common.domain.rule.EventRule;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.restmcu.LatchBoardResource;
import net.awired.housecream.server.it.restmcu.LatchPinResource;
import net.awired.housecream.server.it.restmcu.NotifBuilder;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotify;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotifyCondition;
import net.awired.restmcu.api.domain.pin.RestMcuPinSettings;
import org.junit.Rule;
import org.junit.Test;

public class SwitchPointCreationIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", LatchPinResource.class,
            LatchBoardResource.class);

    @Test
    public void should_update_restmcu_when_adding_rule() throws Exception {
        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1")
                .url("restmcu://127.0.0.1:5879/pin/2").zoneId(42).build();
        Long inPointId = hcs.inPointResource().createInPoint(inPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        hcs.ruleResource().createRule(rule);

        RestMcuPinNotification pinNotif = new NotifBuilder().pinId(2).oldValue(0).value(1).source("127.0.0.1:5879")
                .notify(RestMcuPinNotifyCondition.SUP_OR_EQUAL, 1).build();
        hcs.notifyResource().pinNotification(pinNotif);

        Thread.sleep(1000);

        assertEquals("http://localhost:8080/hcs/ws/", restmcu.getResource(LatchBoardResource.class)
                .getBoardSettings().getNotifyUrl());
        RestMcuPinSettings pinSettings = restmcu.getResource(LatchPinResource.class).getPinSettings(2);
        assertEquals(2, pinSettings.getNotifies().size());
        assertEquals(new RestMcuPinNotify(RestMcuPinNotifyCondition.INF_OR_EQUAL, 0), pinSettings.getNotifies()
                .get(0));
        assertEquals(new RestMcuPinNotify(RestMcuPinNotifyCondition.SUP_OR_EQUAL, 1), pinSettings.getNotifies()
                .get(1));
        assertEquals("my pir1", pinSettings.getName());
    }

}

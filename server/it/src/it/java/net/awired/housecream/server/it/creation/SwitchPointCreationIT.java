package net.awired.housecream.server.it.creation;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.LineInfoBuilder;
import net.awired.housecream.server.it.restmcu.LatchBoardResource;
import net.awired.housecream.server.it.restmcu.LatchLineResource;
import net.awired.housecream.server.it.restmcu.NotifBuilder;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotify;
import net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition;
import net.awired.restmcu.api.domain.line.RestMcuLineSettings;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class SwitchPointCreationIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", LatchLineResource.class,
            LatchBoardResource.class);

    @Test
    @Ignore
    public void should_update_restmcu_when_adding_rule() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(2, new LineInfoBuilder().value(1).build());

        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1")
                .url("restmcu://127.0.0.1:5879/pin/2").zoneId(42).build();
        Long inPointId = hcs.inPointResource().createInPoint(inPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        hcs.ruleResource().createRule(rule);

        RestMcuLineNotification pinNotif = new NotifBuilder().lineId(2).oldValue(0).value(1).source("127.0.0.1:5879")
                .notify(RestMcuLineNotifyCondition.SUP_OR_EQUAL, 1).build();
        hcs.notifyResource().lineNotification(pinNotif);

        Thread.sleep(1000);

        assertEquals("http://localhost:8080/hcs/ws/", restmcu.getResource(LatchBoardResource.class)
                .getBoardSettings().getNotifyUrl());
        RestMcuLineSettings pinSettings = restmcu.getResource(LatchLineResource.class).getLineSettings(2);
        assertThat(pinSettings.getNotifies().size()).isEqualTo(2);
        assertThat(pinSettings.getNotifies().get(0)).isEqualTo(
                new RestMcuLineNotify(RestMcuLineNotifyCondition.INF_OR_EQUAL, 0));
        assertThat(pinSettings.getNotifies().get(1)).isEqualTo(
                new RestMcuLineNotify(RestMcuLineNotifyCondition.SUP_OR_EQUAL, 1));

        assertThat(pinSettings.getName()).isEqualTo("my pir1");
    }
}

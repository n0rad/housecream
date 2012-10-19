package net.awired.housecream.server.it.usecase;

import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static org.fest.assertions.Assertions.assertThat;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.camel.restmcu.LatchBoardResource;
import net.awired.housecream.camel.restmcu.LatchLineResource;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.LineInfoBuilder;
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.housecream.server.it.restmcu.NotifBuilder;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition;
import org.junit.Rule;
import org.junit.Test;

public class MovementDetectorIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchBoardResource(),
            new LatchLineResource());

    @Test
    public void should_turn_on_the_light_when_someone_is_detected() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(2, new LineInfoBuilder().value(1).build());
        restmcu.getResource(LatchLineResource.class).line(3, new LineInfoBuilder().value(1).build());

        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());

        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1").zoneId(landId)
                .url("restmcu://127.0.0.1:5879/2").build();
        Long inPointId = hcs.inPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT).zoneId(landId)
                .url("restmcu://127.0.0.1:5879/3").build();
        Long outPointId = hcs.outPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPointId, 1, ConditionType.event));
        rule.getConsequences().add(new Consequence(outPointId, 1));
        hcs.ruleResource().createRule(rule);

        RestMcuLineNotification pinNotif = new NotifBuilder().lineId(2).oldValue(0).value(1).source("127.0.0.1:5879")
                .notify(RestMcuLineNotifyCondition.SUP_OR_EQUAL, 1).build();
        LatchBoardResource boardResource = restmcu.getResource(LatchBoardResource.class);
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif);

        assertThat(boardResource.awaitUpdateSettings().getNotifyUrl()).isNotNull();
        assertThat(restmcu.getResource(LatchLineResource.class).awaitLineValue(3)).isEqualTo(1);
    }

    @Test
    public void should_toggle_light_state() throws Exception {
        LatchLineResource lineResource = restmcu.getResource(LatchLineResource.class);
        LatchBoardResource boardResource = restmcu.getResource(LatchBoardResource.class);

        lineResource.line(2, new LineInfoBuilder().value(1).build());
        lineResource.line(3, new LineInfoBuilder().value(1).build());

        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());

        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1").zoneId(landId)
                .url("restmcu://127.0.0.1:5879/2").build();
        Long inPointId = hcs.inPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT).zoneId(landId)
                .url("restmcu://127.0.0.1:5879/3").build();
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

        RestMcuLineNotification pinNotif1 = new NotifBuilder().lineId(2).oldValue(0).value(1)
                .source("127.0.0.1:5879").notify(SUP_OR_EQUAL, 1).build();
        RestMcuLineNotification pinNotif0 = new NotifBuilder().lineId(2).oldValue(1).value(0)
                .source("127.0.0.1:5879").notify(SUP_OR_EQUAL, 1).build();

        //notif
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif1);
        assertThat(lineResource.awaitLineValue(3)).isEqualTo(0);

        //notif2
        lineResource.resetValueLatch(3);
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif0);
        assertThat(lineResource.awaitLineValue(3)).isEqualTo(0);

        //notif3
        lineResource.resetValueLatch(3);
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif1);
        assertThat(lineResource.awaitLineValue(3)).isEqualTo(1);
    }
}

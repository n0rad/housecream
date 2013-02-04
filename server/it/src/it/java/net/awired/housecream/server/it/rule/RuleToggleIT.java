package net.awired.housecream.server.it.rule;

import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static org.fest.assertions.api.Assertions.assertThat;
import net.awired.ajsl.test.RestServerRule;
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
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.housecream.server.it.restmcu.NotifBuilder;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.it.builder.LineInfoBuilder;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class RuleToggleIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchBoardResource(),
            new LatchLineResource());

    @Test
    public void should_toggle_light_state() throws Exception {
        LatchLineResource lineResource = restmcu.getResource(LatchLineResource.class);
        LatchBoardResource boardResource = restmcu.getResource(LatchBoardResource.class);

        lineResource.line(2, new LineInfoBuilder().direction(INPUT).value(1).build());
        lineResource.line(3, new LineInfoBuilder().direction(OUTPUT).value(1).build());

        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());

        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1").zoneId(landId)
                .uri("restmcu://127.0.0.1:5879/2").build();
        inPoint = hcs.inPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT).zoneId(landId)
                .uri("restmcu://127.0.0.1:5879/3").build();
        outPoint = hcs.outPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPoint.getId(), 1, ConditionType.event));
        rule.getConditions().add(new Condition(outPoint.getId(), 0, ConditionType.state));
        rule.getConsequences().add(new Consequence(outPoint.getId(), 1));
        hcs.ruleResource().createRule(rule);

        // rule 2
        EventRule rule2 = new EventRule();
        rule2.setName("my first rule2");
        rule2.getConditions().add(new Condition(inPoint.getId(), 1, ConditionType.event));
        rule2.getConditions().add(new Condition(outPoint.getId(), 1, ConditionType.state));
        rule2.getConsequences().add(new Consequence(outPoint.getId(), 0));
        hcs.ruleResource().createRule(rule2);

        RestMcuLineNotification pinNotif1 = new NotifBuilder().lineId(2).oldValue(0).value(1)
                .source("127.0.0.1:5879").notify(SUP_OR_EQUAL, 1).build();
        RestMcuLineNotification pinNotif0 = new NotifBuilder().lineId(2).oldValue(1).value(0)
                .source("127.0.0.1:5879").notify(SUP_OR_EQUAL, 1).build();

        //notif button pushed
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif1);
        assertThat(lineResource.awaitLineValue(3)).isEqualTo(0);

        //notif button released
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif0);

        //notif button pushed
        lineResource.resetValueLatch(3);
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif1);
        assertThat(lineResource.awaitLineValue(3)).isEqualTo(1);

        //notif button released
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif0);

        //notif button pushed
        lineResource.resetValueLatch(3);
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif1);
        assertThat(lineResource.awaitLineValue(3)).isEqualTo(0);
    }

}

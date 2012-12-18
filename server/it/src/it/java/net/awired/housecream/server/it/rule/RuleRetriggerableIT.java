package net.awired.housecream.server.it.rule;

import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static org.fest.assertions.Assertions.assertThat;
import java.util.Date;
import net.awired.ajsl.core.lang.Pair;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.domain.rule.TriggerType;
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

public class RuleRetriggerableIT {
    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchBoardResource(),
            new LatchLineResource());

    @Test
    public void should_retrigger_in_time() throws Exception {
        LatchLineResource lineResource = restmcu.getResource(LatchLineResource.class);
        LatchBoardResource boardResource = restmcu.getResource(LatchBoardResource.class);

        lineResource.line(2, new LineInfoBuilder().direction(INPUT).value(1).build());
        lineResource.line(3, new LineInfoBuilder().direction(OUTPUT).value(1).build());

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
        //        rule.getConditions().add(new Condition(outPointId, 0, ConditionType.state));
        rule.getConsequences().add(new Consequence(outPointId, 1));
        rule.getConsequences().add(new Consequence(outPointId, 0, 5000, TriggerType.RETRIGGER));
        hcs.ruleResource().createRule(rule);

        RestMcuLineNotification pinNotif1 = new NotifBuilder().lineId(2).oldValue(0).value(1)
                .source("127.0.0.1:5879").notify(SUP_OR_EQUAL, 1).build();
        RestMcuLineNotification pinNotif0 = new NotifBuilder().lineId(2).oldValue(1).value(0)
                .source("127.0.0.1:5879").notify(SUP_OR_EQUAL, 1).build();

        Date startPush = new Date();

        //notif button pushed
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif1);
        assertThat(lineResource.awaitLineValue(3)).isEqualTo(1);
        lineResource.resetValueLatch(3);

        //notif button released
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif0);

        // wait 3s and repush the button
        Thread.sleep(3000);
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif1);

        //notif button released
        boardResource.buildNotifyProxyFromNotifyUrl().lineNotification(pinNotif0);

        Pair<Float, Date> awaitLineValueAndDate = lineResource.awaitLineValueAndDate(3);
        assertThat(awaitLineValueAndDate.left).isEqualTo(0);
        assertThat(awaitLineValueAndDate.right.getTime() - startPush.getTime()).isGreaterThan(8000).isLessThan(9000);
    }
}

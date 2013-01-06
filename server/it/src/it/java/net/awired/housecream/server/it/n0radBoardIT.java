package net.awired.housecream.server.it;

import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.rule.Condition;
import net.awired.housecream.server.api.domain.rule.ConditionType;
import net.awired.housecream.server.api.domain.rule.Consequence;
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

@Ignore
public class n0radBoardIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Test
    public void should_() throws Exception {
        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());

        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("light toggle button").zoneId(landId)
                .uri("restmcu://10.1.50.120:80/24").build();
        inPoint = hcs.inPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("light").type(OutPointType.LIGHT).zoneId(landId)
                .uri("restmcu://10.1.50.120:80/44").build();
        outPoint = hcs.outPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("toggle");
        rule.getConditions().add(new Condition(inPoint.getId(), 1, ConditionType.event));
        rule.getConditions().add(new Condition(outPoint.getId(), 0, ConditionType.state));
        rule.getConsequences().add(new Consequence(outPoint.getId(), 1));
        hcs.ruleResource().createRule(rule);

        // rule 2
        EventRule rule2 = new EventRule();
        rule2.setName("toggle2");
        rule2.getConditions().add(new Condition(inPoint.getId(), 1, ConditionType.event));
        rule2.getConditions().add(new Condition(outPoint.getId(), 1, ConditionType.state));
        rule2.getConsequences().add(new Consequence(outPoint.getId(), 0));
        hcs.ruleResource().createRule(rule2);
    }

    @Test
    public void should_testname() throws Exception {
        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());

        // inpoint
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("light switch button").zoneId(landId)
                .uri("restmcu://10.1.50.120:80/24").build();
        inPoint = hcs.inPointResource().createInPoint(inPoint);

        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("light").type(OutPointType.LIGHT).zoneId(landId)
                .uri("restmcu://10.1.50.120:80/44").build();
        outPoint = hcs.outPointResource().createOutPoint(outPoint);

        // rule
        EventRule rule = new EventRule();
        rule.setName("my first rule");
        rule.getConditions().add(new Condition(inPoint.getId(), 1, ConditionType.event));
        rule.getConsequences().add(new Consequence(outPoint.getId(), 1));
        hcs.ruleResource().createRule(rule);

        // rule
        EventRule rule2 = new EventRule();
        rule2.setName("my first rule2");
        rule2.getConditions().add(new Condition(inPoint.getId(), 0, ConditionType.event));
        rule2.getConsequences().add(new Consequence(outPoint.getId(), 0));
        hcs.ruleResource().createRule(rule2);

    }

}

/*
 * 
 * rule "toggle"
 * when
 * Event(pointId == 450, value == 1.0)
 * not PointState(pointId == 449) || PointState(pointId == 449, value == 0.0)
 * not ConsequenceAction(outPointId == (long)449)
 * then
 * insert(new ConsequenceAction((long)449,(float)1.0, 0, null));
 * end
 * 
 * rule "toggle2"
 * when
 * Event(pointId == 450, value == 1.0)
 * not PointState(pointId == 449) || PointState(pointId == 449, value == 1.0)
 * not ConsequenceAction(outPointId == (long)449)
 * then
 * insert(new ConsequenceAction((long)449,(float)0.0, 0, null));
 * end
 */


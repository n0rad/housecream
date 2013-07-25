package org.housecream.server.it.rule;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.housecream.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static org.housecream.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static org.housecream.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static org.housecream.restmcu.it.builder.LineInfoBuilder.line;
import static org.housecream.restmcu.it.builder.NotifBuilder.notif;
import static org.housecream.server.api.domain.inpoint.InPointType.PIR;
import static org.housecream.server.api.domain.outPoint.OutPointType.LIGHT;
import static org.housecream.server.api.domain.rule.ConditionType.event;
import static org.housecream.server.it.builder.ConditionBuilder.condition;
import static org.housecream.server.it.builder.ConsequenceBuilder.consequence;
import java.util.Date;
import org.housecream.restmcu.it.resource.LatchBoardResource;
import org.housecream.restmcu.it.resource.LatchLineResource;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.zone.Land;
import org.housecream.server.it.HcWsItServer;
import org.housecream.server.it.HcWsItSession;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.jaxrs.junit.RestServerRule;

public class RuleDelayerIT {

    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");
    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(INPUT).value(1).build()) //
            .addLine(line(3).direction(OUTPUT).value(1).build());

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_delay_the_consequence() throws Exception {
        HcWsItSession session = hcs.session();
        Land land = session.zone().createLand("land");
        InPoint pir = session.inpoint().create("my pir", land, PIR, "restmcu://127.0.0.1:5879/2");
        OutPoint light = session.outpoint().create("light1", land, LIGHT, "restmcu://127.0.0.1:5879/3");
        session.rule().create("rule", condition(pir, 1, event), consequence(light, 1, 5000));

        board.sendNotif(notif().line(line.lineInfo(2)).val(1).notify(SUP_OR_EQUAL, 1).build());

        Date start = new Date();
        assertThat(line.awaitLineValue(3)).isEqualTo(1);
        Date stop = new Date();
        assertThat(stop.getTime() - start.getTime()).isGreaterThan(5000).isLessThan(5200);
    }
}

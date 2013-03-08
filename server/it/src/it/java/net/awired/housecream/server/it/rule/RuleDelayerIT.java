package net.awired.housecream.server.it.rule;

import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import static net.awired.housecream.server.api.domain.outPoint.OutPointType.LIGHT;
import static net.awired.housecream.server.api.domain.rule.ConditionType.event;
import static net.awired.housecream.server.it.builder.ConditionBuilder.condition;
import static net.awired.housecream.server.it.builder.ConsequenceBuilder.consequence;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static net.awired.restmcu.it.builder.LineInfoBuilder.line;
import static net.awired.restmcu.it.builder.NotifBuilder.notif;
import static org.fest.assertions.api.Assertions.assertThat;
import java.util.Date;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.HcsItSession;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class RuleDelayerIT {

    private LatchBoardResource board = new LatchBoardResource();
    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(INPUT).value(1).build()) //
            .addLine(line(3).direction(OUTPUT).value(1).build());

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_delay_the_consequence() throws Exception {
        HcsItSession session = hcs.session();
        Land land = session.zone().createLand("land");
        InPoint pir = session.inpoint().create("my pir", land, PIR, "restmcu://127.0.0.1:5879/2");
        OutPoint light = session.outpoint().create("light1", land, LIGHT, "restmcu://127.0.0.1:5879/3");
        session.rule().create("rule", condition(pir, 1, event), consequence(light, 1, 5000));

        board.sendNotif(notif().line(line.lineInfo(2)).val(1).source("127.0.0.1:5879").notify(SUP_OR_EQUAL, 1).build());

        Date start = new Date();
        assertThat(line.awaitLineValue(3)).isEqualTo(1);
        Date stop = new Date();
        assertThat(stop.getTime() - start.getTime()).isGreaterThan(5000).isLessThan(5200);
    }
}

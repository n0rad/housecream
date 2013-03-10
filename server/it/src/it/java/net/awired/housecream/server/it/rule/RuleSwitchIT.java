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
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.it.HcWsItServer;
import net.awired.housecream.server.it.HcWsItSession;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class RuleSwitchIT {

    private LatchBoardResource board = new LatchBoardResource();
    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(INPUT).build()) //
            .addLine(line(3).direction(OUTPUT).build());

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_turn_on_the_light_when_someone_is_detected() throws Exception {
        HcWsItSession session = hcs.session();
        Land land = session.zone().createLand("landName");
        InPoint pir = session.inpoint().create("my pir1", land, PIR, "restmcu://127.0.0.1:5879/2");
        OutPoint light = session.outpoint().create("my light1", land, LIGHT, "restmcu://127.0.0.1:5879/3");
        session.rule().create("my first rule", condition(pir, 1, event), consequence(light, 1));

        board.sendNotif(notif().line(line.lineInfo(2)).val(1).source("127.0.0.1:5879").notify(SUP_OR_EQUAL, 1)
                .build());

        assertThat(line.awaitLineValue(3)).isEqualTo(1);
    }
}

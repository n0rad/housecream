package org.housecream.server.it.test.rule;

import static java.util.Arrays.asList;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.housecream.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static org.housecream.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static org.housecream.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static org.housecream.restmcu.it.builder.LineInfoBuilder.line;
import static org.housecream.restmcu.it.builder.NotifBuilder.notif;
import static org.housecream.server.api.domain.point.PointType.LIGHT;
import static org.housecream.server.api.domain.point.PointType.PIR;
import static org.housecream.server.api.domain.rule.ConditionType.event;
import static org.housecream.server.api.domain.rule.TriggerType.RETRIGGER;
import static org.housecream.server.it.builder.ConditionBuilder.condition;
import static org.housecream.server.it.builder.ConsequenceBuilder.consequence;
import java.util.Date;
import org.apache.commons.lang3.tuple.Pair;
import org.housecream.restmcu.api.domain.line.RestMcuLineNotification;
import org.housecream.restmcu.it.resource.LatchBoardResource;
import org.housecream.restmcu.it.resource.LatchLineResource;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.it.core.ItServer;
import org.housecream.server.it.core.ItSession;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.jaxrs.junit.RestServerRule;

public class RuleRetriggerableIT {
    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");
    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(INPUT).value(1).build()) //
            .addLine(line(3).direction(OUTPUT).value(1).build());

    @Rule
    public ItServer hcs = new ItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_retrigger_in_time() throws Exception {
        ItSession session = hcs.session();
        Point pir = session.points().create("my pir", PIR, "restmcu://127.0.0.1:5879/2");
        Point light = session.points().create("light1", LIGHT, "restmcu://127.0.0.1:5879/3");
        session.rules().create("firstrule", condition(pir, 1, event), //
                asList(consequence(light, 1), consequence(light, 0, 5000, RETRIGGER)));

        RestMcuLineNotification pinNotif1 = notif().lineId(2).oldVal(0).val(1).notify(SUP_OR_EQUAL, 1).build();
        RestMcuLineNotification pinNotif0 = notif().lineId(2).oldVal(1).val(0).notify(SUP_OR_EQUAL, 1).build();

        Date startPush = new Date();

        //notif button pushed
        board.sendNotif(pinNotif1);
        assertThat(line.awaitLineValue(3)).isEqualTo(1);
        line.resetValueLatch(3);

        //notif button released
        board.sendNotif(pinNotif0);

        // wait 3s and repush the button
        Thread.sleep(3000);
        board.sendNotif(pinNotif1);

        //notif button released
        board.sendNotif(pinNotif0);

        Pair<Float, Date> awaitLineValueAndDate = line.awaitLineValueAndDate(3);
        assertThat(awaitLineValueAndDate.getLeft()).isEqualTo(0);
        assertThat(awaitLineValueAndDate.getRight().getTime() - startPush.getTime()).isGreaterThan(8000).isLessThan(
                9000);
    }
}

package net.awired.housecream.server.it.rule;

import static java.util.Arrays.asList;
import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import static net.awired.housecream.server.api.domain.outPoint.OutPointType.LIGHT;
import static net.awired.housecream.server.api.domain.rule.ConditionType.event;
import static net.awired.housecream.server.api.domain.rule.TriggerType.NON_RETRIGGER;
import static net.awired.housecream.server.it.builder.ConditionBuilder.condition;
import static net.awired.housecream.server.it.builder.ConsequenceBuilder.consequence;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static net.awired.restmcu.it.builder.LineInfoBuilder.line;
import static net.awired.restmcu.it.builder.NotifBuilder.notif;
import static org.fest.assertions.api.Assertions.assertThat;
import java.util.Date;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.it.HcWsItServer;
import net.awired.housecream.server.it.HcWsItSession;
import net.awired.jaxrs.junit.RestServerRule;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Rule;
import org.junit.Test;

public class RuleNonRetriggerableIT {

    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");
    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(INPUT).value(1).build()) //
            .addLine(line(3).direction(OUTPUT).value(1).build());

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_not_retrigger_on_repush() throws Exception {
        HcWsItSession session = hcs.session();
        Land land = session.zone().createLand("land");
        InPoint pir = session.inpoint().create("my pir", land, PIR, "restmcu://127.0.0.1:5879/2");
        OutPoint light = session.outpoint().create("light1", land, LIGHT, "restmcu://127.0.0.1:5879/3");
        session.rule().create("firstrule", condition(pir, 1, event), //
                asList(consequence(light, 1), consequence(light, 0, 5000, NON_RETRIGGER)));

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
        assertThat(awaitLineValueAndDate.getRight().getTime() - startPush.getTime()).isGreaterThan(5000).isLessThan(
                6000);
    }
}

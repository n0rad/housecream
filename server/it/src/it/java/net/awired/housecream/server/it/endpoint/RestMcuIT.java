package net.awired.housecream.server.it.endpoint;

import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static net.awired.restmcu.it.builder.LineInfoBuilder.line;
import static org.fest.assertions.api.Assertions.assertThat;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.it.HcWsItServer;
import net.awired.housecream.server.it.HcWsItSession;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class RestMcuIT {

    private LatchBoardResource board = new LatchBoardResource();

    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(OUTPUT).value(1).build());

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_update_notify_url_on_creation() throws Exception {
        HcWsItSession session = hcs.session();
        Land land = session.zone().createLand("land");
        session.inpoint().create("my pir", land, PIR, "restmcu://127.0.0.1:5879/2");

        assertThat(board.awaitUpdateSettings().getNotifyUrl()).isNotNull();
    }

    @Test
    @Ignore("I'm not getting the current value for now")
    public void should_access_current_state_after_creation() throws Exception {
        //        restmcu.getResource(LatchLineResource.class).line(3, new LineInfoBuilder().value(1).build());
        //        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        //        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1").zoneId(landId)
        //                .uri("restmcu://127.0.0.1:5879/3").build();
        //        inPoint = hcs.inPointResource().createInPoint(inPoint);
        //
        //        Float pointValue = hcs.inPointResource().getPointValue(inPoint.getId());
        //
        //        assertThat(pointValue).isEqualTo(1f);
    }
}

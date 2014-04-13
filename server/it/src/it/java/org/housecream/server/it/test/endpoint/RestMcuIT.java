package org.housecream.server.it.test.endpoint;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.housecream.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static org.housecream.restmcu.it.builder.LineInfoBuilder.line;
import static org.housecream.server.api.domain.point.PointType.PIR;
import org.housecream.restmcu.it.resource.LatchBoardResource;
import org.housecream.restmcu.it.resource.LatchLineResource;
import org.housecream.server.it.core.ItServer;
import org.housecream.server.it.core.ItSession;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.jaxrs.junit.RestServerRule;

public class RestMcuIT {

    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");

    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(OUTPUT).value(1).build());

    @Rule
    public ItServer hcs = new ItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_update_notify_url_on_creation() throws Exception {
        ItSession session = hcs.session();
        session.points().create("my pir", PIR, "restmcu://127.0.0.1:5879/2");

        assertThat(board.awaitUpdateSettings().getNotifyUrl()).isNotNull();
    }

    @Test
    @Ignore("I'm not getting the current value for now")
    public void should_access_current_state_after_creation() throws Exception {
        //        restmcu.getResource(LatchLineResource.class).line(3, new LineInfoBuilder().value(1).build());
        //        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        //        InPoint point = new PointBuilder().type(InPointType.PIR).name("my pir1").zoneId(landId)
        //                .uri("restmcu://127.0.0.1:5879/3").build();
        //        point = hcs.inPointResource().createInPoint(point);
        //
        //        Float pointValue = hcs.inPointResource().getValue(point.getId());
        //
        //        assertThat(pointValue).isEqualTo(1f);
    }
}

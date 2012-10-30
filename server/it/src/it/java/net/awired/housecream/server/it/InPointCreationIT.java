package net.awired.housecream.server.it;

import static org.fest.assertions.Assertions.assertThat;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.camel.restmcu.LatchBoardResource;
import net.awired.housecream.camel.restmcu.LatchLineResource;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.LineInfoBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class InPointCreationIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchBoardResource(),
            new LatchLineResource());

    @Test
    public void should_update_notify_url_when_create_point() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(3, new LineInfoBuilder().value(1).build());
        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1").zoneId(landId)
                .url("restmcu://127.0.0.1:5879/pin/3").build();

        hcs.inPointResource().createInPoint(inPoint);

        assertThat(restmcu.getResource(LatchBoardResource.class).getBoardSettings().getNotifyUrl()).isNotNull()
                .isNotEmpty();
    }

    @Test
    @Ignore("I'm not getting the current value for now")
    public void should_access_current_state_after_creation() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(3, new LineInfoBuilder().value(1).build());
        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1").zoneId(landId)
                .url("restmcu://127.0.0.1:5879/pin/3").build();
        Long pointId = hcs.inPointResource().createInPoint(inPoint);

        Float pointValue = hcs.inPointResource().getPointValue(pointId);

        assertThat(pointValue).isEqualTo(1f);
    }
}
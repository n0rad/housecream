package net.awired.housecream.server.it;

import static org.fest.assertions.api.Assertions.assertThat;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.restmcu.it.builder.LineInfoBuilder;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class InPointCreationIT {

    @Rule
    public HcWsItServer hc = new HcWsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchBoardResource(),
            new LatchLineResource());

    @Test
    public void should_update_notify_url_when_creating_inpoint() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(3, new LineInfoBuilder().value(1).build());
        Zone land = hc.zonesResource().createZone(new LandBuilder().name("land").build());
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1").zoneId(land.getId())
                .uri("restmcu://127.0.0.1:5879/3").build();

        hc.inPointsResource().createInPoint(inPoint);

        assertThat(restmcu.getResource(LatchBoardResource.class).getBoardSettings().getNotifyUrl()).isNotNull()
                .isNotEmpty();
    }

    @Test
    @Ignore("I'm not getting the current value for now")
    public void should_access_current_state_after_creation() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(3, new LineInfoBuilder().value(1).build());
        Land land = (Land) hc.zonesResource().createZone(new LandBuilder().name("land").build());
        InPoint inPoint = new InPointBuilder().type(InPointType.PIR).name("my pir1").zoneId(land.getId())
                .uri("restmcu://127.0.0.1:5879/3").build();
        inPoint = hc.inPointsResource().createInPoint(inPoint);

        Float pointValue = hc.inPointResource().getPointValue(inPoint.getId());

        assertThat(pointValue).isEqualTo(1f);
    }
}

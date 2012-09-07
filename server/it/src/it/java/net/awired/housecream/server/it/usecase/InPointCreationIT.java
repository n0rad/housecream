package net.awired.housecream.server.it.usecase;

import static org.fest.assertions.Assertions.assertThat;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.domain.inpoint.InPointType;
import net.awired.housecream.server.it.HcsItContext;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.builder.PinInfoBuilder;
import net.awired.housecream.server.it.restmcu.LatchBoardResource;
import net.awired.housecream.server.it.restmcu.LatchPinResource;
import org.junit.Rule;
import org.junit.Test;

public class InPointCreationIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", LatchBoardResource.class,
            LatchPinResource.class);

    @Test
    public void should_update_notify_url_when_create_point() throws Exception {
        restmcu.getResource(LatchPinResource.class).pin(3, new PinInfoBuilder().value(1).build());

        InPoint inPoint = new InPoint();
        inPoint.setType(InPointType.PIR);
        inPoint.setName("my pir1");
        inPoint.setZoneId(42);
        inPoint.setUrl("restmcu://127.0.0.1:5879/pin/3");

        hcs.inPointResource().createInPoint(inPoint);

        assertThat(restmcu.getResource(LatchBoardResource.class).getBoardSettings().getNotifyUrl()).isEqualTo(
                HcsItContext.getUrl() + "/router");
    }

    @Test
    public void should_access_current_state_after_creation() throws Exception {
        restmcu.getResource(LatchPinResource.class).pin(3, new PinInfoBuilder().value(1).build());

        InPoint inPoint = new InPoint();
        inPoint.setType(InPointType.PIR);
        inPoint.setName("my pir1");
        inPoint.setZoneId(42);
        inPoint.setUrl("restmcu://127.0.0.1:5879/pin/3");
        Long pointId = hcs.inPointResource().createInPoint(inPoint);

        Float pointValue = hcs.inPointResource().getPointValue(pointId);

        assertThat(pointValue).isEqualTo(1f);
    }
}

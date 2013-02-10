package net.awired.housecream.server.it.outevent;

import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static org.fest.assertions.api.Assertions.assertThat;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.it.HcWsItServer;
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.restmcu.it.builder.LineInfoBuilder;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class OutEventIT {

    @Rule
    public HcWsItServer hc = new HcWsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchBoardResource(),
            new LatchLineResource());

    @Test
    public void should_send_output_value_synchronously() throws Exception {
        LatchLineResource lineResource = restmcu.getResource(LatchLineResource.class);
        lineResource.line(3, new LineInfoBuilder().direction(OUTPUT).value(0).build());
        // land
        Land land = (Land) hc.zonesResource().createZone(new LandBuilder().name("land").build());
        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT).zoneId(land.getId())
                .uri("restmcu://127.0.0.1:5879/3").build();
        outPoint = hc.outPointsResource().createOutPoint(outPoint);

        hc.outPointResource().setValue(outPoint.getId(), 45f);

        assertThat(restmcu.getResource(LatchLineResource.class).getLineValue(3)).isEqualTo(45f);
    }

}

package net.awired.housecream.server.it.usecase;

import static org.junit.Assert.assertEquals;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.it.builder.PinInfoBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.housecream.server.it.restmcu.LatchBoardResource;
import net.awired.housecream.server.it.restmcu.LatchPinResource;
import org.junit.Rule;
import org.junit.Test;

public class OutPointCreationIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", LatchBoardResource.class,
            LatchPinResource.class);

    @Test
    public void should_update_notify_url_when_create_point() throws Exception {
        restmcu.getResource(LatchPinResource.class).pin(4, new PinInfoBuilder().value(1).build());
        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        OutPoint outPoint = new OutPointBuilder().type(OutPointType.LIGHT).name("myLight").zoneId(landId)
                .url("restmcu://127.0.0.1:5879/pin/4").build();
        Long pointId = hcs.outPointResource().createOutPoint(outPoint);

        Float pointValue = hcs.outPointResource().getPointValue(pointId);

        assertEquals((Float) 1f, pointValue);
    }
}

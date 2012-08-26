package net.awired.housecream.server.it.usecase;

import static org.junit.Assert.assertEquals;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;
import net.awired.housecream.server.common.domain.outPoint.OutPointType;
import net.awired.housecream.server.it.HcsItServer;
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
        OutPoint outPoint = new OutPoint();
        outPoint.setType(OutPointType.LIGHT);
        outPoint.setName("my light");
        outPoint.setZoneId(42);
        outPoint.setUrl("restmcu://127.0.0.1:5879/pin/4");

        Long pointId = hcs.outPointResource().createOutPoint(outPoint);

        Float pointValue = hcs.outPointResource().getPointValue(pointId);

        assertEquals((Float) 1f, pointValue);
    }
}

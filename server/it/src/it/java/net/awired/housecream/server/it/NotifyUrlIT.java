package net.awired.housecream.server.it;

import static net.awired.housecream.server.common.domain.inpoint.InPointType.PIR;
import static org.junit.Assert.assertEquals;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.domain.zone.Land;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.restmcu.LatchBoardResource;
import net.awired.housecream.server.it.restmcu.LatchPinResource;
import net.awired.restmcu.api.domain.board.RestMcuBoardSettings;
import org.junit.Rule;
import org.junit.Test;

public class NotifyUrlIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restMcu = new RestServerRule("http://localhost:5879/", LatchPinResource.class,
            LatchBoardResource.class);

    @Test
    public void should_update_notify_url_on_creation() throws Exception {
        long landId = hcs.zoneResource().createZone(new Land());
        InPoint inPoint = new InPointBuilder().type(PIR).name("my pir1").zoneId(landId)
                .url("restmcu://127.0.0.1:5879/pin/2").build();
        hcs.inPointResource().createInPoint(inPoint);

        RestMcuBoardSettings settings = restMcu.getResource(LatchBoardResource.class).awaitUpdateSettings();

        assertEquals(hcs.context().getUrl() + "/router", settings.getNotifyUrl());
    }
}

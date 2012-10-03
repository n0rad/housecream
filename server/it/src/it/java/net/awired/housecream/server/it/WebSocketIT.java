package net.awired.housecream.server.it;

import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import static org.junit.Assert.assertEquals;
import java.util.List;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.LineInfoBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.housecream.server.it.restmcu.LatchBoardResource;
import net.awired.housecream.server.it.restmcu.LatchLineResource;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotify;
import net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition;
import org.junit.Rule;
import org.junit.Test;

public class WebSocketIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", LatchLineResource.class,
            LatchBoardResource.class);

    @Test
    public void should_notify_client_on_event_received() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(2, new LineInfoBuilder().value(1).build());
        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        InPoint inPoint = new InPointBuilder().type(PIR).name("my pir1").zoneId(landId)
                .url("restmcu://127.0.0.1:5879/pin/2").build();
        long inpointId = hcs.inPointResource().createInPoint(inPoint);
        HcwWebSocket webSocketConnection = hcs.webSocketConnection();
        RestMcuLineNotification notif = new RestMcuLineNotification(2, 0f, 1f, "127.0.0.1:5879",
                new RestMcuLineNotify(RestMcuLineNotifyCondition.SUP_OR_EQUAL, 1f));

        hcs.notifyResource().lineNotification(notif);

        List<Event> events = webSocketConnection.awaitEvents();

        assertEquals(1, events.size());
        assertEquals(inpointId, events.get(0).getPointId());
        assertEquals(1f, events.get(0).getValue(), 0f);
    }
}
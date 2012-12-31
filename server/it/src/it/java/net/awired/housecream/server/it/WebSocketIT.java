package net.awired.housecream.server.it;

import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import static org.junit.Assert.assertEquals;
import java.util.List;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotify;
import net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition;
import net.awired.restmcu.it.builder.LineInfoBuilder;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class WebSocketIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchLineResource(),
            new LatchBoardResource());

    @Test
    public void should_notify_client_on_event_received() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(2, new LineInfoBuilder().value(1).build());
        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        InPoint inPoint = new InPointBuilder().type(PIR).name("my pir1").zoneId(landId)
                .uri("restmcu://127.0.0.1:5879/2").build();
        inPoint = hcs.inPointResource().createInPoint(inPoint);
        HcwWebSocket webSocketConnection = hcs.webSocketConnection();
        RestMcuLineNotification notif = new RestMcuLineNotification(2, 0f, 1f, "127.0.0.1:5879",
                new RestMcuLineNotify(RestMcuLineNotifyCondition.SUP_OR_EQUAL, 1f));

        restmcu.getResource(LatchBoardResource.class).buildNotifyProxyFromNotifyUrl().lineNotification(notif);

        List<Event> events = webSocketConnection.awaitEvents();

        assertEquals(1, events.size());
        assertEquals(inPoint.getId(), (Long) events.get(0).getPointId());
        assertEquals(1f, events.get(0).getValue(), 0f);
    }
}

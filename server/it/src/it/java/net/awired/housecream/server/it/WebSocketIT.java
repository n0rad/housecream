package net.awired.housecream.server.it;

import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static net.awired.restmcu.it.builder.LineInfoBuilder.line;
import static org.junit.Assert.assertEquals;
import java.util.List;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.jaxrs.junit.RestServerRule;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotify;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class WebSocketIT {

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");
    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(INPUT).build());

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", line, board);

    @Test
    public void should_notify_client_on_event_received() throws Exception {
        HcWsItSession session = hcs.session();
        Land land = session.zone().createLand("landName");
        InPoint pir = session.inpoint().create("my pir1", land, PIR, "restmcu://127.0.0.1:5879/2");
        HcWebWebSocket webSocket = session.webSocket();
        RestMcuLineNotification notif = new RestMcuLineNotification(2, 0f, 1f,
                new RestMcuLineNotify(SUP_OR_EQUAL, 1f));
        board.sendNotif(notif);

        List<Event> events = webSocket.awaitEvents();

        assertEquals(1, events.size());
        assertEquals(pir.getId(), (Long) events.get(0).getPointId());
        assertEquals(1f, events.get(0).getValue(), 0f);
    }
}

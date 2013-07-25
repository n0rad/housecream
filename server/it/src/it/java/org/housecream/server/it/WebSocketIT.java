package org.housecream.server.it;

import static org.housecream.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static org.housecream.restmcu.api.domain.line.RestMcuLineNotifyCondition.SUP_OR_EQUAL;
import static org.housecream.restmcu.it.builder.LineInfoBuilder.line;
import static org.housecream.server.api.domain.inpoint.InPointType.PIR;
import static org.junit.Assert.assertEquals;
import java.util.List;
import org.housecream.restmcu.api.domain.line.RestMcuLineNotification;
import org.housecream.restmcu.api.domain.line.RestMcuLineNotify;
import org.housecream.restmcu.it.resource.LatchBoardResource;
import org.housecream.restmcu.it.resource.LatchLineResource;
import org.housecream.server.api.domain.Event;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.zone.Land;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.jaxrs.junit.RestServerRule;

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
        assertEquals(pir.getId(), events.get(0).getPointId());
        assertEquals(1f, events.get(0).getValue(), 0f);
    }
}

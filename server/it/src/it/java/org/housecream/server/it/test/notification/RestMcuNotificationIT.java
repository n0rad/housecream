package org.housecream.server.it.test.notification;

import static org.housecream.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static org.housecream.restmcu.it.builder.LineInfoBuilder.line;
import static org.housecream.server.api.domain.point.PointType.PIR;
import org.housecream.restmcu.api.domain.line.RestMcuLineNotification;
import org.housecream.restmcu.it.resource.LatchBoardResource;
import org.housecream.restmcu.it.resource.LatchLineResource;
import org.housecream.server.it.core.ItServer;
import org.housecream.server.it.core.ItSession;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.jaxrs.junit.RestServerRule;

public class RestMcuNotificationIT {

    @Rule
    public ItServer hcs = new ItServer();

    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");
    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(INPUT).build());

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_not_return_error_on_unknown_event_received() throws Exception {
        ItSession session = hcs.session();
        session.points().create("my pir1", PIR, "restmcu://127.0.0.1:5879/2");

        RestMcuLineNotification lineNotif = new RestMcuLineNotification();
        lineNotif.setSource("unknown source");

        board.sendNotif(lineNotif);
    }

}

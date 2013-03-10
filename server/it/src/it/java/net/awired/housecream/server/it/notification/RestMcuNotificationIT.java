package net.awired.housecream.server.it.notification;

import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.INPUT;
import static net.awired.restmcu.it.builder.LineInfoBuilder.line;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.it.HcWsItServer;
import net.awired.housecream.server.it.HcWsItSession;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class RestMcuNotificationIT {

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    private LatchBoardResource board = new LatchBoardResource();
    private LatchLineResource line = new LatchLineResource() //
            .addLine(line(2).direction(INPUT).build());

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", board, line);

    @Test
    public void should_not_return_error_on_unknown_event_received() throws Exception {
        HcWsItSession session = hcs.session();
        Land land = session.zone().createLand("landName");
        session.inpoint().create("my pir1", land, PIR, "restmcu://127.0.0.1:5879/2");

        RestMcuLineNotification lineNotif = new RestMcuLineNotification();
        lineNotif.setSource("unknown source");

        board.sendNotif(lineNotif);
    }

}

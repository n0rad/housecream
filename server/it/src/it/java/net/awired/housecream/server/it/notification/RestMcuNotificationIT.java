package net.awired.housecream.server.it.notification;

import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.it.HcWsItServer;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.it.builder.LineInfoBuilder;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class RestMcuNotificationIT {

    @Rule
    public HcWsItServer hc = new HcWsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchLineResource(),
            new LatchBoardResource());

    @Test
    public void should_not_return_error_on_unknown_event_received() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(2, new LineInfoBuilder().value(1).build());
        long landId = hc.zoneResource().createZone(new LandBuilder().name("land").build());

        InPoint inPoint = new InPointBuilder().type(PIR).name("my pir1").zoneId(landId)
                .uri("restmcu://127.0.0.1:5879/2").build();
        hc.inPointResource().createInPoint(inPoint);

        RestMcuLineNotification lineNotif = new RestMcuLineNotification();
        lineNotif.setSource("unknown source");

        restmcu.getResource(LatchBoardResource.class).buildNotifyProxyFromNotifyUrl().lineNotification(lineNotif);
    }

}

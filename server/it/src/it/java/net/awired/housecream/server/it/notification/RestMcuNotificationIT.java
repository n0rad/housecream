package net.awired.housecream.server.it.notification;

import net.awired.housecream.server.it.HcsItServer;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import org.junit.Rule;
import org.junit.Test;

public class RestMcuNotificationIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Test
    public void should_not_return_error_on_unknown_event_received() throws Exception {
        RestMcuLineNotification lineNotification = new RestMcuLineNotification();
        lineNotification.setSource("unknown source");

        hcs.notifyResource().lineNotification(lineNotification);
    }

}

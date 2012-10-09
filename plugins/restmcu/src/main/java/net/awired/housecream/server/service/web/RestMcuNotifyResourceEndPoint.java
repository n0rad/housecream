package net.awired.housecream.server.service.web;

import javax.ws.rs.Path;
import net.awired.housecream.server.api.resource.HcRestMcuNotifyResource;
import net.awired.restmcu.api.domain.board.RestMcuBoardNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;
import org.springframework.stereotype.Component;

@Component
@Path(HcRestMcuNotifyResource.INNER_ROUTE_CONTEXT)
public class RestMcuNotifyResourceEndPoint implements RestMcuNotifyResource {

    @Override
    public void lineNotification(RestMcuLineNotification pinNotification) {
    }

    @Override
    public void boardNotification(RestMcuBoardNotification boardNotification) {
    }

    @Override
    public long getPosixTime() {
        return 0;
    }

}

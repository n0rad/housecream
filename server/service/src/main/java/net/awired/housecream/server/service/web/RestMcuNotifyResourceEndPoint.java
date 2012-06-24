package net.awired.housecream.server.service.web;

import javax.ws.rs.Path;
import net.awired.housecream.server.common.resource.HcRestMcuNotifyResource;
import net.awired.restmcu.api.domain.board.RestMcuBoardNotification;
import net.awired.restmcu.api.domain.pin.RestMcuPinNotification;
import net.awired.restmcu.api.resource.server.RestMcuNotifyResource;
import org.springframework.stereotype.Component;

@Component
@Path(HcRestMcuNotifyResource.INNER_ROUTE_CONTEXT)
public class RestMcuNotifyResourceEndPoint implements RestMcuNotifyResource {

    @Override
    public void pinNotification(RestMcuPinNotification pinNotification) {
    }

    @Override
    public void boardNotification(RestMcuBoardNotification boardNotification) {
    }

}

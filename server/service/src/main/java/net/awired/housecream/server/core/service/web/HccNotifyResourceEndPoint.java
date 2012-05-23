package net.awired.housecream.server.core.service.web;

import javax.ws.rs.Path;
import net.awired.housecream.client.common.domain.board.HccBoardNotification;
import net.awired.housecream.client.common.domain.pin.HccPinNotification;
import net.awired.housecream.client.common.resource.server.HccNotifyResource;
import org.springframework.stereotype.Component;

@Component
@Path("/hccNotify")
public class HccNotifyResourceEndPoint implements HccNotifyResource {

    @Override
    public void pinNotification(HccPinNotification pinNotification) {
    }

    @Override
    public void boardNotification(HccBoardNotification boardNotification) {
    }

}

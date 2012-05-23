package net.awired.housecream.client.common.resource.server;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.awired.housecream.client.common.domain.board.HccBoardNotification;
import net.awired.housecream.client.common.domain.pin.HccPinNotification;

public interface HccNotifyResource {

    @PUT
    @Path("/pin")
    public void pinNotification(HccPinNotification pinNotification);

    @PUT
    @Path("/board")
    public void boardNotification(HccBoardNotification boardNotification);

}

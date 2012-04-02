package net.awired.housecream.client.common.resource.server;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import net.awired.housecream.client.common.domain.HccPinNotify;

public interface HccNotifyResource {

    @GET
    public String start();

    @POST
    @Path("notify")
    public void notify(HccPinNotify pinNotify);
}

package net.awired.housecream.client.common.resource.client;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.awired.housecream.client.common.domain.board.HccDevice;
import net.awired.housecream.client.common.resource.HccUpdateException;

@Path("/")
public interface HccResource {

    @GET
    public HccDevice getDeviceInfo();

    @PUT
    public HccDevice updateDevice(HccDevice deviceInfo) throws HccUpdateException;

}

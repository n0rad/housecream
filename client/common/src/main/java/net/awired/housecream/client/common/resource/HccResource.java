package net.awired.housecream.client.common.resource;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.awired.housecream.client.common.domain.HccDevice;

@Path("/")
public interface HccResource {

    //    @DELETE
    //    public void resetClient();

    @GET
    public HccDevice getDeviceInfo();

    @PUT
    public HccDevice updateDevice(HccDevice deviceInfo) throws HccUpdateException;

}

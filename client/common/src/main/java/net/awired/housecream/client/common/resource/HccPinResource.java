package net.awired.housecream.client.common.resource;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.housecream.client.common.domain.HccPinDescription;
import net.awired.housecream.client.common.domain.HccPinInfo;

@Path("pin/{pinId}")
public interface HccPinResource {

    @GET
    HccPinDescription getPinDescription(@PathParam("pinId") int pinId) throws PinNotFoundException;

    @GET
    @Path("/info")
    HccPinInfo getPinInfo(@PathParam("pinId") int pinId) throws PinNotFoundException;

    @PUT
    @Path("/info")
    public void setPinInfo(@PathParam("pinId") int pinId, HccPinInfo pin) throws PinNotFoundException,
            HccUpdateException;

    @GET
    @Path("/value")
    Float getValue(@PathParam("pinId") int pinId) throws PinNotFoundException;

    @PUT
    @Path("/value")
    void setValue(@PathParam("pinId") int pinId, Float value) throws PinNotFoundException, HccUpdateException;
}

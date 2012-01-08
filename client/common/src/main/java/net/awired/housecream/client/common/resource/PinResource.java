package net.awired.housecream.client.common.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.housecream.client.common.domain.HccPin;

@Path("{pinId}")
public interface PinResource {

    @GET
    HccPin getPin(@PathParam("pinId") int pinId) throws PinNotFoundException;
}

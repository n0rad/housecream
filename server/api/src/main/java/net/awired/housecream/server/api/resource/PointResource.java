package net.awired.housecream.server.api.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.ajsl.core.lang.exception.NotFoundException;

public interface PointResource {

    @GET
    @Path("/value")
    Float getPointValue(@PathParam("id") long pointId) throws NotFoundException;

}

package net.awired.housecream.server.api.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.resource.generic.PointResource;

@Path("/outpoints/{id}")
public interface OutPointResource extends PointResource {

    @GET
    OutPoint getOutPoint(@PathParam("id") long outPointId) throws NotFoundException;

    @DELETE
    void deleteOutPoint(@PathParam("id") long outPointId);

    @PUT
    @Path("/value")
    void setValue(@PathParam("id") long outPointId, Float value) throws Exception;

}

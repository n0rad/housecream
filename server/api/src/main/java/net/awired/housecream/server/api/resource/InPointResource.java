package net.awired.housecream.server.api.resource;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;

@Path("/inpoints/{id}")
public interface InPointResource extends PointResource {

    @PUT
    InPoint updateInPoint(@PathParam("id") long inPointId, @Valid InPoint inPoint) throws PluginNotFoundException;

    @GET
    InPoint getInPoint(@PathParam("id") long inPointId) throws NotFoundException;

    @DELETE
    void deleteInPoint(@PathParam("id") long inPointId);

    //    @GET
    //    @Path("{id}/value")
    //    Float getInPointValue(@PathParam("id") long inPointId);

}

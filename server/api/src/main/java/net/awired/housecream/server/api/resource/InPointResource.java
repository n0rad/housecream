package net.awired.housecream.server.api.resource;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.api.domain.inpoint.InPoint;

@Path("/inpoint")
public interface InPointResource extends PointResource {

    @GET
    @Path("/validator")
    public ClientValidatorInfo getInPointValidator();

    @POST
    InPoint createInPoint(@Valid InPoint inPoint) throws PluginNotFoundException;

    @PUT
    @Path("{id}")
    InPoint updateInPoint(@Valid InPoint inPoint) throws PluginNotFoundException;

    @GET
    @Path("{id}")
    InPoint getInPoint(@PathParam("id") long inPointId) throws NotFoundException;

    @DELETE
    @Path("{id}")
    void deleteInPoint(@PathParam("id") long inPointId);

    //    @GET
    //    @Path("{id}/value")
    //    Float getInPointValue(@PathParam("id") long inPointId);

}

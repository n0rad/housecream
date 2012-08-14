package net.awired.housecream.server.common.resource;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.common.domain.inpoint.InPoint;

@Path("/inpoint")
public interface InPointResource extends PointResource {

    @GET
    @Path("/validator")
    public ClientValidatorInfo getInPointValidator();

    @PUT
    long createInPoint(@Valid InPoint inPoint);

    @GET
    @Path("{id}")
    InPoint getInPoint(@PathParam("id") long inPointId) throws NotFoundException;

    @DELETE
    @Path("{id}")
    void deleteInPoint(@PathParam("id") long inPointId);

}

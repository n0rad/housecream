package net.awired.housecream.server.api.resource;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;

@Path("/outpoint")
public interface OutPointResource extends PointResource {

    @PUT
    long createOutPoint(@Valid OutPoint outPoint);

    @GET
    @Path("{id}")
    OutPoint getOutPoint(@PathParam("id") long outPointId) throws NotFoundException;

    @DELETE
    @Path("{id}")
    void deleteOutPoint(@PathParam("id") long outPointId);

    @GET
    @Path("/validator")
    public ClientValidatorInfo getOutPointValidator();

}
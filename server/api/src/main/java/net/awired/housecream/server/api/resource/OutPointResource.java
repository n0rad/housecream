package net.awired.housecream.server.api.resource;

import javax.validation.Valid;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;

@Path("/outpoint")
public interface OutPointResource extends PointResource {

    @PUT
    long createOutPoint(@Valid OutPoint outPoint);

}

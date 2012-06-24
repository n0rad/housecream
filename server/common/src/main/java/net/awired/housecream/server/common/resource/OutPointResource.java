package net.awired.housecream.server.common.resource;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;

@Path("/outpoint")
public interface OutPointResource extends PointResource {

    @PUT
    Long createOutPoint(@Valid OutPoint outPoint);

    @DELETE
    void deleteAllOutPoints();

}

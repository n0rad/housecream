package net.awired.housecream.server.api.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

@Path("/outpoints")
public interface OutPointsResource {

    @DELETE
    void deleteAllOutPoints();
}

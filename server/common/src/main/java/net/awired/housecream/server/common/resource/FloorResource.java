package net.awired.housecream.server.common.resource;

import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.housecream.server.common.domain.Floor;

@Path("/floor")
public interface FloorResource {

    @GET
    List<Floor> getAllFloors();

    @PUT
    Floor createFloor(Floor floor);

    @DELETE
    void deleteFloors();

    @POST
    Floor updateFloor(Floor floor);

    @GET
    @Path("{id}")
    Floor getFloor(@PathParam("id") long id) throws NoFloorFoundException;

    @DELETE
    @Path("{id}")
    void deleteFloor(@PathParam("id") long id);

}

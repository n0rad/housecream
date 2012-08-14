package net.awired.housecream.server.common.resource;

import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.housecream.server.common.domain.zone.Zones;

@Path("/zones")
public interface ZonesResource {

    @GET
    Zones getZones(@QueryParam("length") Integer length, //
            @QueryParam("start") Integer start, //
            @QueryParam("search") String search, //
            @QueryParam("searchProperty") List<String> searchProperties, //
            @QueryParam("order") List<Order> orders);

    @DELETE
    void deleteAllZones();
}

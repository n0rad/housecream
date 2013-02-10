package net.awired.housecream.server.api.resource;

import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.domain.zone.Zones;

@Path("/zones")
public interface ZonesResource {

    @GET
    Zones getZones(@QueryParam("length") Integer length, //
            @QueryParam("start") Integer start, //
            @QueryParam("search") String search, //
            @QueryParam("searchProperty") List<String> searchProperties, //
            @QueryParam("order") List<Order> orders);

    @GET
    @Path("/validator")
    Map<String, ClientValidatorInfo> getZoneValidator();

    @POST
    Zone createZone(@Valid Zone zone) throws RuntimeException;

    @DELETE
    void deleteAllZones();
}

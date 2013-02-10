package net.awired.housecream.server.api.resource;

import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.outPoint.OutPoints;

@Path("/outpoints")
public interface OutPointsResource {

    @GET
    OutPoints getInPoints(@QueryParam("length") Integer length, //
            @QueryParam("start") Integer start, //
            @QueryParam("search") String search, //
            @QueryParam("searchProperty") List<String> searchProperties, //
            @QueryParam("order") List<Order> orders);

    @DELETE
    void deleteAllOutPoints();

    @GET
    @Path("/validator")
    ClientValidatorInfo getOutPointValidator();

    @PUT
    OutPoint createOutPoint(@Valid OutPoint outPoint) throws PluginNotFoundException;

    @GET
    @Path("/types")
    List<OutPointType> getOutPointTypes();
}

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
import net.awired.housecream.server.api.domain.rule.EventRule;
import net.awired.housecream.server.api.domain.rule.Rules;

@Path("/rules")
public interface RulesResource {

    @GET
    Rules getRules(@QueryParam("length") Integer length, //
            @QueryParam("start") Integer start, //
            @QueryParam("search") String search, //
            @QueryParam("searchProperty") List<String> searchProperties, //
            @QueryParam("order") List<Order> orders);

    @DELETE
    void deleteAllRules();

    @PUT
    EventRule createRule(@Valid EventRule rule);

    @GET
    @Path("/validator")
    ClientValidatorInfo getRuleValidator();

}

package net.awired.housecream.server.api.resource;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.api.domain.rule.EventRule;

@Path("/rule")
public interface RuleResource {

    @PUT
    EventRule createRule(@Valid EventRule rule);

    @DELETE
    @Path("{id}")
    void deleteRule(@PathParam("id") long ruleId);

    @GET
    @Path("{id}")
    EventRule getRule(@PathParam("id") long ruleId) throws NotFoundException;

    @GET
    @Path("/validator")
    ClientValidatorInfo getRuleValidator();
}

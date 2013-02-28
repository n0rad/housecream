package net.awired.housecream.server.api.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.rule.EventRule;

@Path("/rules/{id}")
public interface RuleResource {

    @DELETE
    void deleteRule(@PathParam("id") long ruleId);

    @GET
    EventRule getRule(@PathParam("id") long ruleId) throws NotFoundException;

}

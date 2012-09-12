package net.awired.housecream.server.api.resource;

import javax.validation.Valid;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import net.awired.housecream.server.api.domain.rule.EventRule;

@Path("/rule")
public interface RuleResource {

    @PUT
    EventRule createRule(@Valid EventRule rule);

}

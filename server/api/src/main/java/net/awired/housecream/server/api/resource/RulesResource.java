package net.awired.housecream.server.api.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

@Path("/rules")
public interface RulesResource {

    @DELETE
    void deleteAllRules();
}

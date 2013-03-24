package net.awired.housecream.server.api.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/session")
public interface SessionResource {

    @POST
    void createSession(@PathParam("username") String username, @PathParam("password") String password);

    @DELETE
    void destroySession();

}

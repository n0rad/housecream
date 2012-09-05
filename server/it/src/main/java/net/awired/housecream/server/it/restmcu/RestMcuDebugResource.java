package net.awired.housecream.server.it.restmcu;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/debug")
public interface RestMcuDebugResource {

    /**
     * debug call to set value of input pin for client/stub tests only
     */
    @PUT
    @Path("/{pinId}/value")
    void setDebugValue(@PathParam("pinId") int pinId, Float value);

}

package net.awired.housecream.client.common.test;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/debug")
public interface DefaultTestDebugResource {

    /**
     * debug call to set value of input pin for client/stub tests only
     */
    @PUT
    @Path("/{pinId}/value")
    void setDebugValue(@PathParam("pinId") int pinId, Float value);

}

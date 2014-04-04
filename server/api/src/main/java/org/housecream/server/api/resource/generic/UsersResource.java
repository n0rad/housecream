package org.housecream.server.api.resource.generic;


import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.housecream.server.api.exception.UsernameAlreadyExistException;
import org.housecream.server.api.resource.Security;
import org.housecream.server.api.resource.Security.Scopes;
import fr.norad.jaxrs.oauth2.api.User;
import fr.norad.jaxrs.oauth2.api.UserNotFoundException;

@Path("/users")
@Security(Scopes.USER_READ)
public interface UsersResource {

    @POST
    @Security(Scopes.USER_WRITE)
    void create(User user) throws UsernameAlreadyExistException;

    @Path("/{username}")
    UserResource user(@PathParam("username") String username);

    public interface UserResource {
        @GET
        User getUser(@PathParam("username") String username) throws UserNotFoundException;
    }
}

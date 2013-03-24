package net.awired.housecream.server.api.resource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import net.awired.housecream.server.api.domain.user.User;

@Path("/users")
public interface UsersResource {

    @POST
    User createUser(User user);

}

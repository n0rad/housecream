package net.awired.housecream.server.api.resource;

import javax.ws.rs.Path;

@Path("/users/{id:\\d+}")
public interface UserResource {

    //    User updateUser(long userId, User user);

}

package net.awired.housecream.server.api.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import net.awired.ajsl.security.Token;

@Path("/oauth")
public interface OauthResource {

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/json")
    @Path("/token")
    Token tokenRequest(@FormParam("client_id") String clientId, //
            @FormParam("client_secret") String clientSecret, //
            @FormParam("grant_type") String grantType, //
            @FormParam("username") String username, //
            @FormParam("password") String password);

}

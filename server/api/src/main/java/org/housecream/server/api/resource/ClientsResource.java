package org.housecream.server.api.resource;


import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.housecream.server.api.Security;
import org.housecream.server.api.Security.Scopes;
import fr.norad.jaxrs.oauth2.api.Client;
import fr.norad.jaxrs.oauth2.api.ClientNotFoundException;

@Security(Scopes.CLIENT_READ)
@Path("/clients")
public interface ClientsResource {

    @GET
    List<Client> listClients();

    @Path("/{id}")
    ClientResource client(@PathParam("id") String clientId);

    interface ClientResource {
        @PUT
        @Security(Scopes.CLIENT_WRITE)
        @Path("/")
        void saveClient(@PathParam("id") String clientId, Client client);

        @GET
        Client getClient(@PathParam("id") String clientId) throws ClientNotFoundException;

    }
}

package org.housecream.server.it.api;


import org.housecream.server.api.resource.ClientsResource;
import org.housecream.server.it.ItSession;
import fr.norad.jaxrs.oauth2.api.Client;

public class ClientsApi {

    private ItSession session;

    public ClientsApi(ItSession session) {
        this.session = session;
    }

    public void createClient(Client client) {
        session.getServer().getResource(ClientsResource.class, session).client(client.getId()).saveClient(client.getId(), client);
    }
}

package org.housecream.server.resource;


import java.util.List;
import org.housecream.server.api.resource.ClientsResource;
import org.housecream.server.application.JaxrsResource;
import org.housecream.server.storage.security.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import fr.norad.jaxrs.oauth2.api.Client;
import fr.norad.jaxrs.oauth2.api.ClientNotFoundException;

@JaxrsResource
public class ClientsResourceImpl implements ClientsResource {

    @Autowired
    private ClientResource clientResource;

    @Autowired
    private ClientDao clientDao;

    @Override
    public List<Client> listClients() {
        return null;
    }

    @Override
    public ClientResource client(String clientId) {
        return clientResource;
    }

    @JaxrsResource
    public static class ClientResourceImpl implements ClientResource {
        @Autowired
        private ClientDao clientDao;

        @Override
        public void saveClient(String clientId, Client client) {
            clientDao.save(client);
        }

        @Override
        public Client getClient(String clientId) throws ClientNotFoundException {
            return clientDao.findClient(clientId);
        }
    }
}

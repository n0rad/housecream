/**
 *
 *     Copyright (C) Housecream.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package org.housecream.server.storage.security;

import static com.google.common.collect.Sets.newHashSet;
import static fr.norad.jaxrs.oauth2.api.spec.domain.GrantType.password;
import javax.annotation.PostConstruct;
import org.housecream.server.Housecream;
import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.application.security.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import fr.norad.jaxrs.oauth2.api.Client;
import fr.norad.jaxrs.oauth2.api.ClientNotFoundException;
import fr.norad.jaxrs.oauth2.api.spec.domain.GrantType;
import fr.norad.jaxrs.oauth2.core.persistence.ClientRepository;


@Repository
public class ClientDao implements ClientRepository {

    public static final String WEB_CLIENTID_PREFIX = "Housecream Web ";

    private final Session session;
    private final PreparedStatement selectQuery;
    private final PreparedStatement insertQuery;

    @Autowired
    private HcProperties props;

    @Autowired
    public ClientDao(Session session) {
        this.session = session;
        selectQuery = session.prepare("SELECT * FROM clients WHERE id = ?");
        insertQuery = session.prepare("BEGIN BATCH INSERT INTO " +
                "clients(id, secret, refreshTokenLifetime, allowedGrantTypes, allowedScopes) " +
                "VALUES(?,?,?,?,?) APPLY BATCH");
    }

    public Client currentWebClient() throws ClientNotFoundException {
        return findClient(currentwebClientName());
    }

    private String currentwebClientName() {
        return WEB_CLIENTID_PREFIX + Housecream.HOUSECREAM.getVersion();
    }

    @PostConstruct
    private void postConstruct() {
        try {
            findClient(currentwebClientName());
        } catch (ClientNotFoundException e) {
            Client webClient = new Client();
            webClient.setId(currentwebClientName());
            webClient.setSecret(new RandomStringGenerator().base64(props.getSecurityRandomStringLength()));
            webClient.setDescription("Housecream default web interface");
            webClient.setTokenLifetimeSecond(props.getSecurityDefaultTokenLifetimeSeconds());
            webClient.setRefreshTokenLifetimeSecond(props.getSecurityDefaultRefreshTokenLifetimeSeconds());
            webClient.setAllowedGrantTypes(newHashSet(password));
            webClient.setAllowedScopes(newHashSet("ANY"));
            save(webClient);
        }

    }

    public void save(Client client) {
        session.execute(insertQuery.bind(client.getId(),
                client.getSecret(),
                client.getRefreshTokenLifetimeSecond(),
                GrantType.toStrings(client.getAllowedGrantTypes()),
                client.getAllowedScopes()));
    }

    @Override
    public Client findClient(String clientId) throws ClientNotFoundException {
        ResultSet execute = session.execute(selectQuery.bind(clientId));
        if (execute.isExhausted()) {
            throw new ClientNotFoundException("Cannot found client with id : " + clientId);
        }
        return map(execute.one());
    }

    private Client map(Row row) {
        Client client = new Client();
        client.setId(row.getString("id"));
        client.setSecret(row.getString("secret"));
        client.setAllowedGrantTypes(GrantType.fromStrings(row.getSet("allowedGrantTypes", String.class)));
        client.setAllowedScopes(row.getSet("allowedScopes", String.class));
        return client;
    }

}

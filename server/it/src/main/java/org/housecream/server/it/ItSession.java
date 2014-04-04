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
package org.housecream.server.it;

import org.housecream.server.it.api.ClientsApi;
import org.housecream.server.it.api.InpointsApi;
import org.housecream.server.it.api.OutpointsApi;
import org.housecream.server.it.api.PropertiesApi;
import org.housecream.server.it.api.RulesApi;
import org.housecream.server.it.api.UsersApi;
import org.housecream.server.it.api.ZonesApi;
import fr.norad.jaxrs.client.server.rest.RestSession;

public class ItSession extends RestSession<ItSession, ItClient> {

    private final ItServer server;

    public ItSession(ItServer server) {
        this.server = server;
    }

    public ItSession(ItServer server, String username, String password) {
        super(username, password);
        this.server = server;
    }

    public HcWebSocket webSocket() {
        return server.newWebSocket().open();
    }

    public ZonesApi zones() {
        return new ZonesApi(this);
    }

    public InpointsApi inpoints() {
        return new InpointsApi(this);
    }

    public OutpointsApi outpoints() {
        return new OutpointsApi(this);
    }

    public RulesApi rules() {
        return new RulesApi(this);
    }

    public ClientsApi clients() {
        return new ClientsApi(this);
    }

    public PropertiesApi props() {
        return new PropertiesApi(this);
    }

    public UsersApi users() {
        return new UsersApi(this);
    }

    ///////////////////////////////////////////

    public ItServer getServer() {
        return server;
    }

}

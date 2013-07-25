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

import org.housecream.server.it.api.InpointApi;
import org.housecream.server.it.api.OauthApi;
import org.housecream.server.it.api.OutpointApi;
import org.housecream.server.it.api.RuleApi;
import org.housecream.server.it.api.ZoneApi;
import fr.norad.jaxrs.client.server.rest.RestSession;
import fr.norad.jaxrs.oauth2.Token;

public class HcWsItSession extends RestSession<HcWsItSession, HcWsItClient> {

    private final HcWsItServer server;
    private String username;
    private String password;

    public HcWsItSession(HcWsItServer server) {
        this.server = server;
    }

    public HcWsItSession(HcWsItServer server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }

    public void authenticate() {
        Token token = oauth().userCredentialAuth(username, password);
        //TODO
        //        this.setToken(token);
    }

    public HcWebWebSocket webSocket() {
        return server.newWebSocket().open();
    }

    public OauthApi oauth() {
        return new OauthApi(this);
    }

    public ZoneApi zone() {
        return new ZoneApi(this);
    }

    public InpointApi inpoint() {
        return new InpointApi(this);
    }

    public OutpointApi outpoint() {
        return new OutpointApi(this);
    }

    public RuleApi rule() {
        return new RuleApi(this);
    }

    ///////////////////////////////////////////

    public HcWsItServer getServer() {
        return server;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}

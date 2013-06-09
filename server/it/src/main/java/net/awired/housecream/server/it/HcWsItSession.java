package net.awired.housecream.server.it;

import net.awired.ajsl.security.Token;
import net.awired.ajsl.ws.rest.RestSession;
import net.awired.housecream.server.it.api.InpointApi;
import net.awired.housecream.server.it.api.OauthApi;
import net.awired.housecream.server.it.api.OutpointApi;
import net.awired.housecream.server.it.api.RuleApi;
import net.awired.housecream.server.it.api.ZoneApi;

public class HcWsItSession extends RestSession<HcWsItSession, HcWsItClient> {

    private final HcWsItServer server;
    private final String username;
    private final String password;

    public HcWsItSession(HcWsItServer server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }

    public void authenticate() {
        Token token = oauth().userCredentialAuth(username, password);
        this.setToken(token);
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

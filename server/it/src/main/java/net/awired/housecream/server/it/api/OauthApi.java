package net.awired.housecream.server.it.api;

import net.awired.ajsl.security.Token;
import net.awired.housecream.server.api.resource.OauthResource;
import net.awired.housecream.server.it.HcWsItSession;

public class OauthApi {

    private final HcWsItSession session;

    public OauthApi(HcWsItSession session) {
        this.session = session;
    }

    public Token userCredentialAuth(String username, String password) {
        OauthResource resource = session.getServer().getResource(OauthResource.class, session);
        Token token = resource.tokenRequest("clientId", "secret", "password", username, password);
        return token;
    }

}

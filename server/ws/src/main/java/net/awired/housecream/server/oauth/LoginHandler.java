package net.awired.housecream.server.oauth;

import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.owner.ResourceOwnerLoginHandler;

public class LoginHandler implements ResourceOwnerLoginHandler {

    @Override
    public UserSubject createSubject(String name, String password) {
        return new UserSubject(name);
    }

}

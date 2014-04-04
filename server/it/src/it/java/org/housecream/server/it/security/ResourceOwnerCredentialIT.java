package org.housecream.server.it.security;

import org.assertj.core.api.Assertions;
import org.housecream.server.it.ItServer;
import org.housecream.server.it.ItSession;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.jaxrs.oauth2.api.SecurityUnauthorizedException;
import fr.norad.jaxrs.oauth2.api.User;

public class ResourceOwnerCredentialIT {

    @Rule
    public ItServer hcs = new ItServer();


    @Test(expected = SecurityUnauthorizedException.class)
    public void should_throw_exception_if_no_authorized() {
        hcs.session().props().getProperties();
    }

    @Test
    public void should_increment_failed_attempts() throws Exception {
        User user = new User();
        user.setUsername("n0rad");
        user.setPassword("password");

        ItSession adminSession = hcs.session("admin", "admin");
        adminSession.users().create(user);

        try {
            hcs.session("n0rad", "badpassword").users().get("n0rad");
        } catch (Exception e) {
            System.out.println("yop");
        }

        Assertions.assertThat(adminSession.users().get("n0rad").getFailedLoginAttempt()).isEqualTo(1);
    }
}

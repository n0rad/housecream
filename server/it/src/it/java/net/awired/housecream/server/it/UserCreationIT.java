package net.awired.housecream.server.it;

import static org.fest.assertions.api.Assertions.assertThat;
import net.awired.housecream.server.api.domain.user.User;
import net.awired.housecream.server.api.resource.SessionResource;
import org.junit.Rule;
import org.junit.Test;

public class UserCreationIT {

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    @Test
    public void should_create_user_and_authenticate() throws Exception {
        User user = hcs.user().create("n0rad", "password");

        SessionResource resource = hcs.getResource(SessionResource.class, new HcWsItSession(hcs));
        resource.createSession("n0rad", "password");

        assertThat(user.getPassword()).isNull();
        assertThat(user.getHashedPassword()).isNull();
    }

    @Test
    public void should_auth() throws Exception {
        //        User user = hcs.user().create("n0rad", "password");

        SessionResource resource = hcs.getResource(SessionResource.class, new HcWsItSession(hcs));
        resource.createSession("n0rad", "password");

        //        assertThat(user.getPassword()).isNull();
        //        assertThat(user.getHashedPassword()).isNull();
    }
}

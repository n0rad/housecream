package org.housecream.server.it.security;

import java.util.List;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.it.HcWsItServer;
import org.housecream.server.it.HcWsItSession;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class ResourceOwnerCredentialIT {

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    @Test
    @Ignore("not implemented yet")
    public void should_create_user_and_authenticate() throws Exception {
        HcWsItSession session = hcs.session("n0rad", "password");

        session.authenticate();

        List<InPoint> list = session.inpoint().list();

        //        assertThat(token.getAccessToken()).isNotNull();

    }
}

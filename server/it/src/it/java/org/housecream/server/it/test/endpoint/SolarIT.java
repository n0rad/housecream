package org.housecream.server.it.test.endpoint;

import org.housecream.server.it.core.ItServer;
import org.junit.Rule;

public class SolarIT {

    @Rule
    public ItServer hcs = new ItServer();

    public void should_get_current_luminosity() {
        hcs.session().points().create("solar point", )
    }


}

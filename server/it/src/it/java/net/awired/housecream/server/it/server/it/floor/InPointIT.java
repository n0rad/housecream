package net.awired.housecream.server.it.server.it.floor;

import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.it.HcWsItServer;
import org.junit.Rule;
import org.junit.Test;

public class InPointIT {

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    @Test
    public void should_success_on_delete_not_existing_point() {
        hcs.session().inpoint().internalInpointResource().deleteInPoint(50L);
    }

    @Test(expected = NotFoundException.class)
    public void should_not_found_not_exists_point() throws Exception {
        hcs.session().inpoint().getPoint(-1L);
    }
}

package org.housecream.server.it.server.it.floor;

import java.util.UUID;
import org.housecream.server.it.HcWsItServer;
import org.junit.Rule;
import org.junit.Test;
import fr.norad.core.lang.exception.NotFoundException;

public class InPointIT {

    @Rule
    public HcWsItServer hcs = new HcWsItServer();

    @Test
    public void should_success_on_delete_not_existing_point() {
        hcs.session().inpoint().internalInpointResource().deleteInPoint(UUID.randomUUID());
    }

    @Test(expected = NotFoundException.class)
    public void should_not_found_not_exists_point() throws Exception {
        hcs.session().inpoint().getPoint(UUID.randomUUID());
    }
}
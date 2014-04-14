package org.housecream.server.it.test;

import java.util.UUID;
import org.housecream.server.api.exception.PointNotFoundException;
import org.housecream.server.it.core.ItServer;
import org.junit.Rule;
import org.junit.Test;

public class InPointIT {

    @Rule
    public ItServer hcs = new ItServer();

    @Test
    public void should_success_on_delete_not_existing_point() throws Exception {
        UUID inPointId = UUID.randomUUID();
        hcs.session().points().internalInpointResource(inPointId).deletePoint(inPointId);
    }

    @Test(expected = PointNotFoundException.class)
    public void should_not_found_not_exists_point() throws Exception {
        hcs.session().points().getPoint(UUID.randomUUID());
    }
}

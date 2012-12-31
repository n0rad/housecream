package net.awired.housecream.server.it.server.it.floor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.net.URI;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.Point;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.it.HcsItServer;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

public class InPointIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Test
    public void should_success_on_delete_not_existing_point() {
        hcs.inPointResource().deleteInPoint(50L);
    }

    @Test
    @Ignore
    public void should_create_point() throws Exception {

        InPoint point = new InPoint();
        point.setName("my point name");
        point.setUri(new URI("genre style ouda")); //TODO use builder

        point = hcs.inPointResource().createInPoint(point);

        assertNotNull(point);

        Point getPoint = hcs.inPointResource().getInPoint(point.getId());

        assertTrue(EqualsBuilder.reflectionEquals(point, getPoint));
    }

    @Test(expected = NotFoundException.class)
    public void should_not_found_not_exists_point() throws Exception {
        hcs.inPointResource().getInPoint(-1L);
    }
}

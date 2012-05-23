package net.awired.housecream.server.it.floor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.common.domain.Point;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.it.HcsTestRule;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Rule;
import org.junit.Test;

public class InPointIT {

    @Rule
    public HcsTestRule hcs = new HcsTestRule();

    @Test
    public void should_success_on_delete_not_existing_point() {
        hcs.getInPointResource().deleteInPoint(50L);
    }

    @Test
    public void should_create_point() throws Exception {
        InPoint point = new InPoint();
        point.setName("my point name");
        point.setUrl("genre style ouda");

        Point createPoint = hcs.getInPointResource().createInPoint(point);

        assertNotNull(createPoint.getId());

        Point getPoint = hcs.getInPointResource().getInPoint(createPoint.getId());

        assertTrue(EqualsBuilder.reflectionEquals(createPoint, getPoint));
    }

    @Test(expected = NotFoundException.class)
    public void should_not_found_not_exists_point() throws Exception {
        hcs.getInPointResource().getInPoint(-1L);
    }
}

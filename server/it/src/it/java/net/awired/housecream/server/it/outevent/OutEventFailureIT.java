package net.awired.housecream.server.it.outevent;

import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static org.fest.assertions.Assertions.assertThat;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.restmcu.it.builder.LineInfoBuilder;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class OutEventFailureIT {
    private static final String CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE = "Cannot reach the line to set the value";

    public final class FailSetLatchLineResource extends LatchLineResource {
        @Override
        public void setLineValue(Integer lineId, Float value) throws NotFoundException, UpdateException {
            throw new RuntimeException(CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE);
        }
    }

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchBoardResource(),
            new FailSetLatchLineResource());

    @Test
    public void should_send_failure_if_cannot_set_output() throws Exception {
        LatchLineResource lineResource = restmcu.getResource(LatchLineResource.class);
        lineResource.line(3, new LineInfoBuilder().direction(OUTPUT).value(0).build());
        // land
        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        // outpoint
        OutPoint outPoint = new OutPointBuilder().name("my light1").type(OutPointType.LIGHT).zoneId(landId)
                .uri("restmcu://127.0.0.1:5879/3").build();
        outPoint = hcs.outPointResource().createOutPoint(outPoint);

        try {
            hcs.outPointResource().setValue(outPoint.getId(), 45f);
        } catch (Exception e) {
            assertThat(e).hasMessage(CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE);
            assertThat(e).isExactlyInstanceOf(RuntimeException.class);
            return;
        }
        Assert.fail();
    }
}

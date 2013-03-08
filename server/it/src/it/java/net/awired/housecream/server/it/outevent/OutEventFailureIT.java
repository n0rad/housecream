package net.awired.housecream.server.it.outevent;

import static net.awired.housecream.server.api.domain.outPoint.OutPointType.LIGHT;
import static net.awired.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static net.awired.restmcu.it.builder.LineInfoBuilder.line;
import static org.fest.assertions.api.Assertions.assertThat;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.core.lang.exception.UpdateException;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.zone.Land;
import net.awired.housecream.server.it.HcsItServer;
import net.awired.housecream.server.it.HcsItSession;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class OutEventFailureIT {
    private static final String CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE = "Cannot reach the line to set the value";

    public final class FailSetLatchLineResource extends LatchLineResource {
        public FailSetLatchLineResource() {
            addLine(line(3).direction(OUTPUT).build());
        }

        @Override
        public void setLineValue(Integer lineId, Float value) throws NotFoundException, UpdateException {
            throw new RuntimeException(CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE);
        }
    }

    private FailSetLatchLineResource line = new FailSetLatchLineResource();

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchBoardResource(), line);

    @Test
    public void should_send_failure_if_cannot_set_output() throws Exception {
        HcsItSession session = hcs.session();
        Land land = session.zone().createLand("landName");
        OutPoint light = session.outpoint().create("light1", land, LIGHT, "restmcu://127.0.0.1:5879/3");

        try {
            session.outpoint().setValue(light, 45f);
        } catch (Exception e) {
            assertThat(e).hasMessage(CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE);
            assertThat(e).isExactlyInstanceOf(RuntimeException.class);
            return;
        }
        Assert.fail();
    }
}

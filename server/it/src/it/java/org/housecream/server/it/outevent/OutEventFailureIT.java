package org.housecream.server.it.outevent;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.housecream.restmcu.api.domain.line.RestMcuLineDirection.OUTPUT;
import static org.housecream.restmcu.it.builder.LineInfoBuilder.line;
import static org.housecream.server.api.domain.outPoint.OutPointType.LIGHT;
import org.housecream.restmcu.api.LineNotFoundException;
import org.housecream.restmcu.api.RestMcuUpdateException;
import org.housecream.restmcu.it.resource.LatchBoardResource;
import org.housecream.restmcu.it.resource.LatchLineResource;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.zone.Land;
import org.housecream.server.it.ItServer;
import org.housecream.server.it.ItSession;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import fr.norad.jaxrs.client.server.resource.mapper.HttpStatusExceptionMapper;
import fr.norad.jaxrs.client.server.rest.RestBuilder;
import fr.norad.jaxrs.junit.RestServerRule;

public class OutEventFailureIT {
    private static final String CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE = "Cannot reach the line to set the value";

    public final class FailSetLatchLineResource extends LatchLineResource {
        public FailSetLatchLineResource() {
            addLine(line(3).direction(OUTPUT).build());
        }

        @Override
        public void setLineValue(Integer lineId, Float value) throws LineNotFoundException, RestMcuUpdateException {
            throw new RestMcuUpdateException(CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE);
        }
    }

    private FailSetLatchLineResource line = new FailSetLatchLineResource();
    private LatchBoardResource board = new LatchBoardResource("127.0.0.1:5879");

    @Rule
    public ItServer hcs = new ItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule(new RestBuilder()
            .addProvider(new JacksonJsonProvider())
            .addProvider(new HttpStatusExceptionMapper()),
            "http://localhost:5879/", board, line);

    @Test
    public void should_send_failure_if_cannot_set_output() throws Exception {
        ItSession session = hcs.session().asJson();
        Land land = session.zones().createLand("landName");
        OutPoint light = session.outpoints().create("light1", land, LIGHT, "restmcu://127.0.0.1:5879/3");

        try {
            session.outpoints().setValue(light, 45f);
        } catch (Exception e) {
            assertThat(e).hasMessage(CANNOT_REACH_THE_LINE_TO_SET_THE_VALUE);
            assertThat(e).isExactlyInstanceOf(RuntimeException.class);
            return;
        }
        Assert.fail();
    }
}

package net.awired.housecream.server.it;

import static net.awired.housecream.server.api.domain.inpoint.InPointType.PIR;
import static org.fest.assertions.Assertions.assertThat;
import net.awired.ajsl.test.RestServerRule;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.it.builder.zone.LandBuilder;
import net.awired.restmcu.api.domain.board.RestMcuBoardSettings;
import net.awired.restmcu.it.builder.LineInfoBuilder;
import net.awired.restmcu.it.resource.LatchBoardResource;
import net.awired.restmcu.it.resource.LatchLineResource;
import org.junit.Rule;
import org.junit.Test;

public class NotifyUrlIT {

    @Rule
    public HcsItServer hcs = new HcsItServer();

    @Rule
    public RestServerRule restmcu = new RestServerRule("http://localhost:5879/", new LatchLineResource(),
            new LatchBoardResource());

    @Test
    public void should_update_notify_url_on_creation() throws Exception {
        restmcu.getResource(LatchLineResource.class).line(2, new LineInfoBuilder().value(1).build());

        long landId = hcs.zoneResource().createZone(new LandBuilder().name("land").build());
        InPoint inPoint = new InPointBuilder().type(PIR).name("my pir1").zoneId(landId)
                .url("restmcu://127.0.0.1:5879/2").build();
        hcs.inPointResource().createInPoint(inPoint);

        RestMcuBoardSettings settings = restmcu.getResource(LatchBoardResource.class).awaitUpdateSettings();

        assertThat(settings.getNotifyUrl()).isNotNull();
    }
}

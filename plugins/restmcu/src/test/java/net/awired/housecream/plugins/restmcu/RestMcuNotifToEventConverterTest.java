package net.awired.housecream.plugins.restmcu;

import static net.awired.restmcu.api.domain.line.RestMcuLineNotifyCondition.INF_OR_EQUAL;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import net.awired.housecream.server.api.domain.Event;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.engine.InPointDaoInterface;
import net.awired.restmcu.api.domain.line.RestMcuLineNotification;
import net.awired.restmcu.api.domain.line.RestMcuLineNotify;
import org.apache.camel.Exchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RestMcuNotifToEventConverterTest {

    @Mock
    private InPointDaoInterface inPointDao;

    @InjectMocks
    RestMcuNotifToEventConverter restMcuNotifToEventConverter = new RestMcuNotifToEventConverter();

    @Test
    public void should_remove_port_part_if_80() throws Exception {
        Exchange exchange = mock(Exchange.class);
        InPoint inpoint = new InPoint();
        inpoint.setId(45L);
        RestMcuLineNotification notif = new RestMcuLineNotification(43, 0f, 1f, "192.168.42.42:80",
                new RestMcuLineNotify(INF_OR_EQUAL, 1));
        when(inPointDao.findFromUrl("restmcu://192.168.42.42/43")).thenReturn(inpoint);

        Event event = restMcuNotifToEventConverter.toEvent(notif, exchange);

        assertThat(event.getPointId()).isEqualTo(45L);
    }
}

package net.awired.housecream.server.service;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InPointServiceTest {

    @Mock
    private EngineProcessor processor;

    @Mock
    private InPointDao inPointDao;

    @InjectMocks
    private InPointService service;

    @Test
    public void should_fill_state_in_point_before_return() throws Exception {
        InPoint inpoint = new InPointBuilder().build();
        when(inPointDao.find(42L)).thenReturn(inpoint);
        when(processor.getPointState(42L)).thenReturn(44.44f);

        InPoint inPoint = service.getInPoint(42L);

        assertThat(inPoint.getValue()).isEqualTo(44.44f);
    }
}

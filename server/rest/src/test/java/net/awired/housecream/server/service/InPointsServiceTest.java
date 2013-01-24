package net.awired.housecream.server.service;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPoints;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.it.builder.InPointBuilder;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class InPointsServiceTest {

    @Mock
    private InPointDao inPointDao;

    @Mock
    private EngineProcessor engine;

    @InjectMocks
    private InPointsService service;

    @Test
    public void should_add_value_before_return() throws Exception {
        List<InPoint> value = Arrays.asList(new InPointBuilder().id(43).build());
        when(inPointDao.findFiltered(null, null, null, null, null)).thenReturn(value);
        when(engine.getPointState(43)).thenReturn(42f);

        InPoints inPoints = service.getInPoints(null, null, null, null, null);

        assertThat(inPoints.getInPoints().get(0).getValue()).isEqualTo(42);
    }
}

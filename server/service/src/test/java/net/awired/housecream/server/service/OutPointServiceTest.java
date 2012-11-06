package net.awired.housecream.server.service;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.it.builder.OutPointBuilder;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OutPointServiceTest {

    @Mock
    private OutPointDao outPointDao;

    @Mock
    private EngineProcessor engine;

    @InjectMocks
    private OutPointService service;

    @Test
    public void should_set_value_before_return() throws Exception {
        OutPoint value = new OutPointBuilder().build();
        when(outPointDao.find(42L)).thenReturn(value);
        when(engine.getPointState(42L)).thenReturn(44.44f);

        OutPoint outPoint = service.getOutPoint(42L);

        assertThat(outPoint.getValue()).isEqualTo(44.44f);
    }
}

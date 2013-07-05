/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package net.awired.housecream.server.service;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.UUID;
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
        UUID outPointId = UUID.randomUUID();
        OutPoint value = new OutPointBuilder().build();
        when(outPointDao.find(outPointId)).thenReturn(value);
        when(engine.getPointState(outPointId)).thenReturn(44.44f);

        OutPoint outPoint = service.getOutPoint(outPointId);

        assertThat(outPoint.getValue()).isEqualTo(44.44f);
    }
}

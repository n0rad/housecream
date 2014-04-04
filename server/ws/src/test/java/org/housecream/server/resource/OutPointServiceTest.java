/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.server.resource;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.UUID;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.it.builder.OutPointBuilder;
import org.housecream.server.storage.dao.OutPointDao;
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
    private OutPointResourceImpl service;

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

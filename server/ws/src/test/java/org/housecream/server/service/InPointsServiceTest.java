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
package org.housecream.server.service;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.it.builder.InPointBuilder;
import org.housecream.server.storage.dao.InPointDao;
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
        UUID inPointId = UUID.randomUUID();

        List<InPoint> value = Arrays.asList(new InPointBuilder().id(inPointId).build());
        when(inPointDao.findFiltered(null, null)).thenReturn(value);
        when(engine.getPointState(inPointId)).thenReturn(42f);

        List<InPoint> inPoints = service.getInPoints(null, null, null, null, null);

        assertThat(inPoints.get(0).getValue()).isEqualTo(42);
    }
}

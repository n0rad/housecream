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
public class InPointServiceTest {

    @Mock
    private EngineProcessor processor;

    @Mock
    private InPointDao inPointDao;

    @InjectMocks
    private InPointResourceImpl service;

    @Test
    public void should_fill_state_in_point_before_return() throws Exception {
        UUID inPointId = UUID.randomUUID();

        InPoint inpoint = new InPointBuilder().build();
        when(inPointDao.find(inPointId)).thenReturn(inpoint);
        when(processor.getPointState(inPointId)).thenReturn(44.44f);

        InPoint inPoint = service.getInPoint(inPointId);

        assertThat(inPoint.getValue()).isEqualTo(44.44f);
    }
}

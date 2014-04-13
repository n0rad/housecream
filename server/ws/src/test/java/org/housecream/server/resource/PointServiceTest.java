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

import static org.mockito.Mockito.when;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.it.builder.PointBuilder;
import org.housecream.server.storage.dao.PointDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PointServiceTest {

    @Mock
    private EngineProcessor processor;

    @Mock
    private PointDao pointDao;

    @InjectMocks
    private PointResourceImpl service;

    @Test
    public void should_fill_state_in_point_before_return() throws Exception {
        UUID inPointId = UUID.randomUUID();

        Point inpoint = new PointBuilder().build();
        when(pointDao.find(inPointId)).thenReturn(inpoint);
        when(processor.getPointState(inPointId)).thenReturn(44.44f);

        Point inpoint2 = service.getPoint(inPointId);

        Assertions.assertThat(inpoint2.getValue()).isEqualTo(44.44f);
    }
}

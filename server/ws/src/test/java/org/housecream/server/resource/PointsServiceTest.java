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
import java.util.Arrays;
import java.util.List;
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
public class PointsServiceTest {

    @Mock
    private PointDao pointDao;

    @Mock
    private EngineProcessor engine;

    @InjectMocks
    private PointsResourceImpl service;

    @Test
    public void should_add_value_before_return() throws Exception {
        UUID inPointId = UUID.randomUUID();

        List<Point> value = Arrays.asList(new PointBuilder().id(inPointId).build());
        when(pointDao.findAll()).thenReturn(value);
        when(engine.getPointState(inPointId)).thenReturn(42f);

        List<Point> inPoints = service.getPoints();

        Assertions.assertThat(inPoints.get(0).getValue()).isEqualTo(42);
    }
}

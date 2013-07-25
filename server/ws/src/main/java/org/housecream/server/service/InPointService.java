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

import java.util.UUID;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.resource.InPointResource;
import org.housecream.server.api.resource.InPointsResource;
import org.housecream.server.api.resource.PluginNotFoundException;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.router.DynamicRouteManager;
import org.housecream.server.storage.dao.InPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import fr.norad.core.lang.exception.NotFoundException;

@Service
@Validated
public class InPointService implements InPointResource {

    @Autowired
    private InPointDao pointDao;

    @Autowired
    private DynamicRouteManager routeManager;

    @Autowired
    private EngineProcessor engine;

    @Autowired
    private InPointsResource inPointsService;

    @Override
    public InPoint getInPoint(UUID inPointId) throws NotFoundException {
        inPointsService.deleteAllInPoints();

        InPoint point = pointDao.find(inPointId);
        try {
            point.setValue(engine.getPointState(inPointId));
        } catch (NotFoundException e) {
            // no current value for point
        }
        return point;
    }

    @Override
    public void deleteInPoint(UUID inPointId) {
        try {
            InPoint point = pointDao.find(inPointId);
            routeManager.removeInRoute(point);
            pointDao.delete(point.getId());
        } catch (NotFoundException e) {
            // nothing to do if trying to remove a point that is already not there
        }
    }

    @Override
    public Float getPointValue(UUID pointId) throws NotFoundException {
        return engine.getPointState(pointId);
    }

    @Override
    public InPoint updateInPoint(UUID pointId, InPoint inPoint) throws PluginNotFoundException {
        if (pointId.equals(inPoint.getId())) {
            throw new IllegalStateException("pointId do not match payload sent");
        }
        return inPointsService.createInPoint(inPoint);
    }
}

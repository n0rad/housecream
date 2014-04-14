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

import java.util.UUID;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.exception.PointNotFoundException;
import org.housecream.server.api.resource.PointsResource.PointResource;
import org.housecream.server.application.JaxrsResource;
import org.housecream.server.command.CommandService;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.engine.OutEvent;
import org.housecream.server.router.DynamicRouteManager;
import org.housecream.server.storage.dao.PointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@JaxrsResource
@Validated
public class PointResourceImpl implements PointResource {

    @Autowired
    private PointDao pointDao;

    @Autowired
    private DynamicRouteManager routeManager;

    @Autowired
    private EngineProcessor engine;

    @Autowired
    private CommandService commandService;

    @Override
    public Point getPoint(UUID pointId) throws PointNotFoundException {
        Point point = pointDao.find(pointId);
        try {
            point.setValue(engine.getPointState(pointId));
        } catch (PointNotFoundException e) {
            // no current value for point
        }
        return point;
    }

    @Override
    public void deletePoint(UUID pointId) {
        try {
            Point point = pointDao.find(pointId);
            routeManager.removeInRoute(point);
            pointDao.delete(point.getId());
        } catch (PointNotFoundException e) {
            // nothing to do if trying to remove a point that is already not there
        }
    }

    @Override
    public Float getValue(UUID pointId) throws PointNotFoundException {
        return engine.getPointState(pointId);
    }

    @Override
    public Point updatePoint(UUID pointId, Point inPoint) throws PluginNotFoundException, PointNotFoundException {
        throw new IllegalStateException("not implemented");
//        if (pointId.equals(point.getId())) {
//            throw new IllegalStateException("pointId do not match payload content");
//        }
//        return inPointsService.createInPoint(point);
    }

    @Override
    public void setValue(UUID outPointId, Object value) throws Exception {
        commandService.processOutEvent(new OutEvent(outPointId, (Float) value));
    }
}

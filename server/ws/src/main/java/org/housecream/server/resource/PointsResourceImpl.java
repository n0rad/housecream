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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.point.PointType;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.resource.PointsResource;
import org.housecream.server.application.JaxRsResource;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.router.DynamicRouteManager;
import org.housecream.server.service.PluginService;
import org.housecream.server.storage.dao.PointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.client.bean.validation.js.service.ValidationService;
import fr.norad.core.lang.exception.NotFoundException;

@JaxRsResource
@Validated
public class PointsResourceImpl implements PointsResource {

    @Autowired
    private PointDao pointDao;

    @Autowired
    private EngineProcessor engine;

    @Autowired
    private DynamicRouteManager routeManager;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private PointResource inPointResource;

    @PostConstruct
    public void postConstruct() {
        List<Point> findAll = pointDao.findAll();
        for (Point inPoint : findAll) {
            routeManager.registerInRoute(inPoint);
        }
    }

    @Override
    public ClientValidatorInfo getPointValidator() {
        return validationService.getValidatorInfo(Point.class);
    }

    private void removePreviousRoute(Point inPoint) {
        if (inPoint.getId() != null) {
            try {
                Point previous = pointDao.find(inPoint.getId());
                routeManager.removeInRoute(previous);
            } catch (NotFoundException e) {
                routeManager.removeInRoute(inPoint);
            }
        }
    }

    @Override
    public Point createInPoint(Point inPoint) throws PluginNotFoundException {
        inPoint.setUri(pluginService.validateAndNormalizeURI(inPoint.getUri()));
        removePreviousRoute(inPoint);
        inPoint = pointDao.save(inPoint);
        routeManager.registerInRoute(inPoint);
        return inPoint;
    }

    @Override
    public void deleteAllPoints() {
        List<Point> findAll = pointDao.findAll();
        for (Point point : findAll) {
            routeManager.removeInRoute(point);
            engine.removePointState(point.getId());
            pointDao.delete(point.getId());
        }
    }

    @Override
    public List<Point> getPoints() {
        List<Point> inPoints = pointDao.findAll();
        for (Point inPoint : inPoints) {
            try {
                inPoint.setValue(engine.getPointState(inPoint.getId()));
            } catch (NotFoundException e) {
                // nothing to do if we don't have the value in holder
            }
        }
        return inPoints;
    }

    @Override
    public List<PointType> getPointTypes() {
        return Arrays.asList(PointType.values());
    }

    @Override
    public PointResource point(UUID pointId) {
        return inPointResource;
    }

}

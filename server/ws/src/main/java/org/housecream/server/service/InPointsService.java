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

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.inpoint.InPointType;
import org.housecream.server.api.resource.InPointsResource;
import org.housecream.server.api.resource.PluginNotFoundException;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.router.DynamicRouteManager;
import org.housecream.server.storage.dao.InPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.client.bean.validation.js.service.ValidationService;
import fr.norad.core.lang.exception.NotFoundException;

@Service
@Validated
public class InPointsService implements InPointsResource {

    @Autowired
    private InPointDao inPointDao;

    @Autowired
    private EngineProcessor engine;

    @Autowired
    private DynamicRouteManager routeManager;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private PluginService pluginService;

    @PostConstruct
    public void postConstruct() {
        List<InPoint> findAll = inPointDao.findAll();
        for (InPoint inPoint : findAll) {
            routeManager.registerInRoute(inPoint);
        }
    }

    @Override
    public ClientValidatorInfo getInPointValidator() {
        return validationService.getValidatorInfo(InPoint.class);
    }

    private void removePreviousRoute(InPoint inPoint) {
        if (inPoint.getId() != null) {
            try {
                InPoint previous = inPointDao.find(inPoint.getId());
                routeManager.removeInRoute(previous);
            } catch (NotFoundException e) {
                routeManager.removeInRoute(inPoint);
            }
        }
    }

    @Override
    public InPoint createInPoint(InPoint inPoint) throws PluginNotFoundException {
        inPoint.setUri(pluginService.validateAndNormalizeURI(inPoint.getUri()));
        removePreviousRoute(inPoint);
        inPoint = inPointDao.save(inPoint);
        routeManager.registerInRoute(inPoint);
        return inPoint;
    }

    @Override
    public void deleteAllInPoints() {
        List<InPoint> findAll = inPointDao.findAll();
        for (InPoint point : findAll) {
            routeManager.removeInRoute(point);
            engine.removePointState(point.getId());
            inPointDao.delete(point.getId());
        }
    }

    @Override
    public List<InPoint> getInPoints() {
        List<InPoint> inPoints = inPointDao.findAll();
        for (InPoint inPoint : inPoints) {
            try {
                inPoint.setValue(engine.getPointState(inPoint.getId()));
            } catch (NotFoundException e) {
                // nothing to do if we don't have the value in holder
            }
        }
        return inPoints;
    }

    @Override
    public List<InPointType> getInPointTypes() {
        return Arrays.asList(InPointType.values());
    }

}

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

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ws.rs.POST;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.Order;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.inpoint.InPoints;
import net.awired.housecream.server.api.resource.InPointsResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.router.DynamicRouteManager;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

    @POST
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
    public InPoints getInPoints(Integer length, Integer start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<InPoint> inPointsFiltered = inPointDao.findFiltered(length, start, search, searchProperties, orders);
        for (InPoint inPoint : inPointsFiltered) {
            try {
                inPoint.setValue(engine.getPointState(inPoint.getId()));
            } catch (NotFoundException e) {
                // nothing to do if we don't have the value in holder
            }
        }
        Long count = inPointDao.findFilteredCount(search, searchProperties);
        return new InPoints(inPointsFiltered, count);
    }

    @Override
    public List<InPointType> getInPointTypes() {
        return Arrays.asList(InPointType.values());
    }

}

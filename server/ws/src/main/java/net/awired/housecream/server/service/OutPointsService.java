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
import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.outPoint.OutPoints;
import net.awired.housecream.server.api.resource.OutPointsResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class OutPointsService implements OutPointsResource {

    @Inject
    private OutPointDao outPointDao;

    @Inject
    private EngineProcessor engine;

    @Inject
    private ValidationService validationService;

    @Inject
    private PluginService pluginService;

    @PostConstruct
    public void postConstruct() {
        List<OutPoint> findAll = outPointDao.findAll();
        //        for (OutPoint outPoint : findAll) {
        //            routeManager.registerPointRoute(outPoint);
        //        }
    }

    @Override
    public void deleteAllOutPoints() {
        List<OutPoint> findAll = outPointDao.findAll();
        for (OutPoint point : findAll) {
            //            routeManager.removeInRoute(point);
            engine.removePointState(point.getId());
            outPointDao.delete(point.getId());
        }
    }

    @Override
    public OutPoints getInPoints(Integer length, Integer start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<OutPoint> outPointsFiltered = outPointDao.findFiltered(length, start, search, searchProperties, orders);
        for (OutPoint outPoint : outPointsFiltered) {
            try {
                engine.getPointState(outPoint.getId());
            } catch (NotFoundException e) {
                // nothing to do
            }
        }
        Long count = outPointDao.findFilteredCount(search, searchProperties);
        return new OutPoints(outPointsFiltered, count);
    }

    @Override
    public OutPoint createOutPoint(OutPoint outPoint) throws PluginNotFoundException {
        pluginService.validateAndNormalizeURI(outPoint.getUri());
        outPointDao.save(outPoint);
        return outPoint;
    }

    @Override
    public ClientValidatorInfo getOutPointValidator() {
        return validationService.getValidatorInfo(InPoint.class);
    }

    @Override
    public List<OutPointType> getOutPointTypes() {
        return Arrays.asList(OutPointType.values());
    }
}

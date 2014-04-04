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
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.outPoint.OutPointType;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.resource.OutPointsResource;
import org.housecream.server.application.JaxRsResource;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.service.PluginService;
import org.housecream.server.storage.dao.OutPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.client.bean.validation.js.service.ValidationService;
import fr.norad.core.lang.exception.NotFoundException;

@JaxRsResource
@Validated
public class OutPointsResourceImpl implements OutPointsResource {

    @Autowired
    private OutPointDao outPointDao;

    @Autowired
    private EngineProcessor engine;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private PluginService pluginService;

    @Autowired
    private OutPointResource outPointResource;

    //    @PostConstruct
    //    public void postConstruct() {
    //        List<OutPoint> findAll = outPointDao.findAll();
    //        for (OutPoint outPoint : findAll) {
    //            routeManager.registerPointRoute(outPoint);
    //        }
    //    }

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
    public List<OutPoint> getInPoints() {
        List<OutPoint> outPoints = outPointDao.findAll();
        for (OutPoint outPoint : outPoints) {
            try {
                engine.getPointState(outPoint.getId());
            } catch (NotFoundException e) {
                // nothing to do
            }
        }
        return outPoints;
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

    @Override
    public OutPointResource outPoint(UUID outPointId) {
        return outPointResource;
    }
}

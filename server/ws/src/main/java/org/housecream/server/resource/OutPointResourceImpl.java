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
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.resource.OutPointsResource.OutPointResource;
import org.housecream.server.application.JaxRsResource;
import org.housecream.server.command.CommandService;
import org.housecream.server.engine.EngineProcessor;
import org.housecream.server.engine.OutEvent;
import org.housecream.server.storage.dao.OutPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import fr.norad.core.lang.exception.NotFoundException;

@JaxRsResource
@Validated
public class OutPointResourceImpl implements OutPointResource {

    @Autowired
    private OutPointDao pointDao;

    @Autowired
    private EngineProcessor engine;

    @Autowired
    private CommandService commandService;

    @Override
    public Float getPointValue(UUID pointId) throws NotFoundException {
        return engine.getPointState(pointId);
    }

    @Override
    public OutPoint getOutPoint(UUID outPointId) throws NotFoundException {
        OutPoint find = pointDao.find(outPointId);
        try {
            find.setValue(engine.getPointState(outPointId));
        } catch (NotFoundException e) {
            // no current value for point
        }
        return find;
    }

    @Override
    public void deleteOutPoint(UUID outPointId) {
        try {
            OutPoint point = pointDao.find(outPointId);
            pointDao.delete(point.getId());
        } catch (NotFoundException e) {
            // nothing to do if trying to remove a point that is already not there
        }
    }

    @Override
    public void setValue(UUID outPointId, Float value) throws Exception {
        commandService.processOutEvent(new OutEvent(outPointId, value));
    }
}

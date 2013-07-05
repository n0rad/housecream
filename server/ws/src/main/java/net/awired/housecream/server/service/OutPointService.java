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

import java.util.UUID;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.resource.OutPointResource;
import net.awired.housecream.server.command.CommandService;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.OutEvent;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class OutPointService implements OutPointResource {

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

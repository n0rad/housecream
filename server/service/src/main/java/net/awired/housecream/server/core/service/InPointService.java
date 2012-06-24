package net.awired.housecream.server.core.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.EntityNotFoundException;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.resource.InPointResource;
import net.awired.housecream.server.core.engine.EngineProcessor;
import net.awired.housecream.server.core.engine.StateHolder;
import net.awired.housecream.server.core.router.RouteManager;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class InPointService implements InPointResource {

    @Inject
    private InPointDao pointDao;

    @Inject
    private RouteManager routeManager;

    @Inject
    private StateHolder stateHolder;

    @Inject
    private EngineProcessor engine;

    @Override
    public Long createInPoint(InPoint inPoint) {
        pointDao.save(inPoint);
        routeManager.registerPoint(inPoint);
        engine.registerPoint(inPoint);
        return inPoint.getId();
    }

    @Override
    public InPoint getInPoint(long inPointId) throws NotFoundException {
        try {
            return pointDao.find(inPointId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("No Point with id : " + inPointId);
        }
    }

    @Override
    public void deleteInPoint(long inPointId) {
        pointDao.delete(inPointId);
    }

    @Override
    public void deleteAllInPoints() {
        pointDao.deleteAll();
    }

    @Override
    public Float getPointValue(long pointId) throws NotFoundException {
        return stateHolder.getState(pointId);
    }

}

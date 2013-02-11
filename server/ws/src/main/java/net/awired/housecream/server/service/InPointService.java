package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.resource.InPointResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.router.DynamicRouteManager;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class InPointService implements InPointResource {

    @Inject
    private InPointDao pointDao;

    @Inject
    private DynamicRouteManager routeManager;

    @Inject
    private EngineProcessor engine;

    @Inject
    private InPointsService inPointsService;

    @Override
    public InPoint getInPoint(long inPointId) throws NotFoundException {
        InPoint point = pointDao.find(inPointId);
        try {
            point.setValue(engine.getPointState(inPointId));
        } catch (NotFoundException e) {
            // no current value for point
        }
        return point;
    }

    @Override
    public void deleteInPoint(long inPointId) {
        try {
            InPoint point = pointDao.find(inPointId);
            routeManager.removeInRoute(point);
            pointDao.delete(point.getId());
        } catch (NotFoundException e) {
            // nothing to do if trying to remove a point that is already not there
        }
    }

    @Override
    public Float getPointValue(long pointId) throws NotFoundException {
        return engine.getPointState(pointId);
    }

    @Override
    public InPoint updateInPoint(long pointId, InPoint inPoint) throws PluginNotFoundException {
        if (pointId != inPoint.getId()) {
            throw new IllegalStateException("pointId do not match payload sent");
        }
        return inPointsService.createInPoint(inPoint);
    }

}

package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.resource.InPointResource;
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
    private ValidationService validationService;

    @Override
    public long createInPoint(InPoint inPoint) {
        pointDao.save(inPoint);
        routeManager.registerInRoute(inPoint);
        return inPoint.getId();
    }

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
    public ClientValidatorInfo getInPointValidator() {
        return validationService.getValidatorInfo(InPoint.class);
    }

}

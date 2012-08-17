package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.resource.InPointResource;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.StateHolder;
import net.awired.housecream.server.router.RouteManager;
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

    @Inject
    private ValidationService validationService;

    @Override
    //    @Transactional(propagation = Propagation.REQUIRED)
    public long createInPoint(InPoint inPoint) {
        pointDao.save(inPoint);
        routeManager.registerPoint(inPoint);
        engine.registerPoint(inPoint);
        return inPoint.getId();
    }

    @Override
    public InPoint getInPoint(long inPointId) throws NotFoundException {
        return pointDao.find(inPointId);
    }

    @Override
    public void deleteInPoint(long inPointId) {
        pointDao.delete(inPointId);
    }

    @Override
    public Float getPointValue(long pointId) throws NotFoundException {
        return stateHolder.getState(pointId);
    }

    @Override
    public ClientValidatorInfo getInPointValidator() {
        return validationService.getValidatorInfo(InPoint.class);
    }

}

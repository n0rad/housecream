package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.resource.OutPointResource;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.StateHolder;
import net.awired.housecream.server.router.RouteManager;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Transactional
public class OutPointService implements OutPointResource {

    @Inject
    private OutPointDao pointDao;

    @Inject
    private StateHolder stateHolder;

    @Inject
    private RouteManager routeManager;

    @Inject
    private EngineProcessor engine;

    @Inject
    private ValidationService validationService;

    @Override
    public long createOutPoint(OutPoint outPoint) {
        pointDao.save(outPoint);
        routeManager.registerPoint(outPoint);
        engine.registerPoint(outPoint);
        return outPoint.getId();
    }

    @Override
    public Float getPointValue(long pointId) throws NotFoundException {
        return stateHolder.getState(pointId);
    }

    @Override
    public ClientValidatorInfo getOutPointValidator() {
        return validationService.getValidatorInfo(InPoint.class);
    }

    @Override
    public OutPoint getOutPoint(long outPointId) throws NotFoundException {
        OutPoint find = pointDao.find(outPointId);
        find.setValue(stateHolder.getState(outPointId));
        return find;
    }

    @Override
    public void deleteOutPoint(long outPointId) {
        pointDao.delete(outPointId);
    }
}

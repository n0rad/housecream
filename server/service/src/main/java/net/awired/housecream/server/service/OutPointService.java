package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.resource.OutPointResource;
import net.awired.housecream.server.engine.EngineProcessor;
import net.awired.housecream.server.engine.StateHolder;
import net.awired.housecream.server.router.RouteManager;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class OutPointService implements OutPointResource {

    @Inject
    private OutPointDao outPointDao;

    @Inject
    private StateHolder stateHolder;

    @Inject
    private RouteManager routeManager;

    @Inject
    private EngineProcessor engine;

    @Override
    public long createOutPoint(OutPoint outPoint) {
        outPointDao.save(outPoint);
        routeManager.registerPoint(outPoint);
        engine.registerPoint(outPoint);
        return outPoint.getId();
    }

    @Override
    public Float getPointValue(long pointId) throws NotFoundException {
        return stateHolder.getState(pointId);
    }
}

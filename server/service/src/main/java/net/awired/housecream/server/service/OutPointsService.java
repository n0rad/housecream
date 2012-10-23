package net.awired.housecream.server.service;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.outPoint.OutPoints;
import net.awired.housecream.server.api.resource.OutPointsResource;
import net.awired.housecream.server.engine.StateHolder;
import net.awired.housecream.server.router.RouteManager;
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
    private StateHolder stateHolder;

    @Inject
    private RouteManager routeManager;

    @PostConstruct
    public void postConstruct() {
        List<OutPoint> findAll = outPointDao.findAll();
        for (OutPoint outPoint : findAll) {
            routeManager.registerPointRoute(outPoint);
        }
    }

    @Override
    public void deleteAllOutPoints() {
        List<OutPoint> findAll = outPointDao.findAll();
        for (OutPoint point : findAll) {
            routeManager.removePointRoute(point);
            stateHolder.removeState(point.getId());
            outPointDao.delete(point.getId());
        }
    }

    @Override
    public OutPoints getInPoints(Integer length, Integer start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<OutPoint> outPointsFiltered = outPointDao.findFiltered(length, start, search, searchProperties, orders);
        for (OutPoint outPoint : outPointsFiltered) {
            try {
                stateHolder.getState(outPoint.getId());
            } catch (NotFoundException e) {
                // nothing to do
            }
        }
        Long count = outPointDao.findFilteredCount(search, searchProperties);
        return new OutPoints(outPointsFiltered, count);
    }

    @Override
    public List<OutPointType> getOutPointTypes() {
        return Arrays.asList(OutPointType.values());
    }
}

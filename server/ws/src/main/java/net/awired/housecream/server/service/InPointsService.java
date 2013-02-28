package net.awired.housecream.server.service;

import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.client.bean.validation.js.service.ValidationService;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.inpoint.InPoints;
import net.awired.housecream.server.api.resource.InPointsResource;
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
public class InPointsService implements InPointsResource {

    @Inject
    private InPointDao inPointDao;

    @Inject
    private EngineProcessor engine;

    @Inject
    private DynamicRouteManager routeManager;

    @Inject
    private ValidationService validationService;

    @Inject
    private PluginService pluginService;

    @PostConstruct
    public void postConstruct() {
        List<InPoint> findAll = inPointDao.findAll();
        for (InPoint inPoint : findAll) {
            routeManager.registerInRoute(inPoint);
        }
    }

    @Override
    public ClientValidatorInfo getInPointValidator() {
        return validationService.getValidatorInfo(InPoint.class);
    }

    private void removePreviousRoute(InPoint inPoint) {
        if (inPoint.getId() != null) {
            try {
                InPoint previous = inPointDao.find(inPoint.getId());
                routeManager.removeInRoute(previous);
            } catch (NotFoundException e) {
                routeManager.removeInRoute(inPoint);
            }
        }
    }

    @Override
    public InPoint createInPoint(InPoint inPoint) throws PluginNotFoundException {
        inPoint.setUri(pluginService.validateAndNormalizeURI(inPoint.getUri()));
        removePreviousRoute(inPoint);
        inPoint = inPointDao.save(inPoint);
        routeManager.registerInRoute(inPoint);
        return inPoint;
    }

    @Override
    public void deleteAllInPoints() {
        List<InPoint> findAll = inPointDao.findAll();
        for (InPoint point : findAll) {
            routeManager.removeInRoute(point);
            engine.removePointState(point.getId());
            inPointDao.delete(point.getId());
        }
    }

    @Override
    public InPoints getInPoints(Integer length, Integer start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<InPoint> inPointsFiltered = inPointDao.findFiltered(length, start, search, searchProperties, orders);
        for (InPoint inPoint : inPointsFiltered) {
            try {
                inPoint.setValue(engine.getPointState(inPoint.getId()));
            } catch (NotFoundException e) {
                // nothing to do if we don't have the value in holder
            }
        }
        Long count = inPointDao.findFilteredCount(search, searchProperties);
        return new InPoints(inPointsFiltered, count);
    }

    @Override
    public List<InPointType> getInPointTypes() {
        return Arrays.asList(InPointType.values());
    }

}

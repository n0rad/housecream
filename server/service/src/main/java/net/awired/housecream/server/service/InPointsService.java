package net.awired.housecream.server.service;

import java.util.List;
import javax.inject.Inject;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPoints;
import net.awired.housecream.server.api.resource.InPointsResource;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class InPointsService implements InPointsResource {

    @Inject
    private InPointDao inPointDao;

    @Override
    public void deleteAllInPoints() {
        inPointDao.deleteAll();
    }

    @Override
    public InPoints getInPoints(Integer length, Integer start, String search, List<String> searchProperties,
            List<Order> orders) {
        List<InPoint> inPointsFiltered = inPointDao.findFiltered(length, start, search, searchProperties, orders);
        Long count = inPointDao.findFilteredCount(search, searchProperties);
        return new InPoints(inPointsFiltered, count);
    }

}

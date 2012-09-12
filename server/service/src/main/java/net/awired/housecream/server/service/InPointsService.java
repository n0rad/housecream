package net.awired.housecream.server.service;

import java.util.List;
import javax.inject.Inject;
import net.awired.ajsl.persistence.entity.Order;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
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

    //    @PostConstruct
    public void postConstrut() {
        {
            InPoint object = new InPoint();
            object.setName("point1");
            object.setType(InPointType.HUMIDITY);
            object.setUrl("there1");
            inPointDao.persist(object);
        }

        {
            InPoint object = new InPoint();
            object.setName("point2");
            object.setType(InPointType.KEYPAD);
            object.setUrl("there2");
            inPointDao.persist(object);
        }
        {
            InPoint object = new InPoint();
            object.setName("point3");
            object.setType(InPointType.LOCK);
            object.setUrl("there3");
            inPointDao.persist(object);
        }
        {
            InPoint object = new InPoint();
            object.setName("point4");
            object.setType(InPointType.PIR);
            object.setUrl("there4");
            inPointDao.persist(object);
        }

    }

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

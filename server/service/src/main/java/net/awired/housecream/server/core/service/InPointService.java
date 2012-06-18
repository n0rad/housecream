package net.awired.housecream.server.core.service;

import javax.inject.Inject;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.EntityNotFoundException;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import net.awired.housecream.server.common.resource.InPointResource;
import net.awired.housecream.server.storage.dao.InPointDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class InPointService implements InPointResource {

    @Inject
    private InPointDao pointDao;

    @Override
    public InPoint createInPoint(InPoint inPoint) {
        return pointDao.save(inPoint);
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
}

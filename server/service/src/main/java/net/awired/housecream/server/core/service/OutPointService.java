package net.awired.housecream.server.core.service;

import javax.inject.Inject;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;
import net.awired.housecream.server.common.resource.OutPointResource;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class OutPointService implements OutPointResource {

    @Inject
    private OutPointDao outPointDao;

    @Override
    public OutPoint createOutPoint(OutPoint outPoint) {
        outPointDao.persist(outPoint);
        return outPoint;
    }

    @Override
    public void deleteAllOutPoints() {
        outPointDao.deleteAll();
    }
}

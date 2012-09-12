package net.awired.housecream.server.service;

import javax.inject.Inject;
import net.awired.housecream.server.api.resource.OutPointsResource;
import net.awired.housecream.server.storage.dao.OutPointDao;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class OutPointsService implements OutPointsResource {

    @Inject
    private OutPointDao outPointDao;

    @Override
    public void deleteAllOutPoints() {
        outPointDao.deleteAll();
    }
}

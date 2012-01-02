package net.awired.housecream.server.core.service;

import javax.inject.Inject;
import net.awired.housecream.server.storage.dao.EndPointDao;
import net.awired.housecream.server.storage.entity.EndPoint;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

    @Inject
    private EndPointDao endPointDao;

    public void test(EndPoint endPoint) {
        endPointDao.persist(endPoint);
    }

}

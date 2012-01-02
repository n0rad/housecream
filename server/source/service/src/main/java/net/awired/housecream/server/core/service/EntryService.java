package net.awired.housecream.server.core.service;

import net.awired.housecream.server.storage.dao.EndPointDaoImpl;
import net.awired.housecream.server.storage.entity.EndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

    @Autowired
    private EndPointDaoImpl endPointDao;

    public void test(EndPoint endPoint) {
        endPointDao.persist(endPoint);
    }

}

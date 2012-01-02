package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.GenericDAOImpl;
import net.awired.housecream.server.storage.entity.EndPoint;
import org.springframework.stereotype.Repository;

@Repository
public class EndPointDaoImpl extends GenericDAOImpl<EndPoint, Long> {

    public EndPointDaoImpl() {
        super(EndPoint.class, Long.class);
    }

}

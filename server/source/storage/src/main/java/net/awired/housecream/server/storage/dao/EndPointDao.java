package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.GenericDAOImpl;
import net.awired.housecream.server.storage.entity.PointEntity;
import org.springframework.stereotype.Repository;

@Repository
public class EndPointDao extends GenericDAOImpl<PointEntity, Long> {

    public EndPointDao() {
        super(PointEntity.class, Long.class);
    }

}

package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.GenericDAOImpl;
import net.awired.housecream.server.common.domain.outPoint.OutPoint;
import org.springframework.stereotype.Repository;

@Repository
public class OutPointDao extends GenericDAOImpl<OutPoint, Long> {

    public OutPointDao() {
        super(OutPoint.class, Long.class);
    }
}

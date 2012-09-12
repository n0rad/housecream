package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import org.springframework.stereotype.Repository;

@Repository
public class OutPointDao extends GenericDaoImpl<OutPoint, Long> {

    public OutPointDao() {
        super(OutPoint.class, Long.class);
    }
}

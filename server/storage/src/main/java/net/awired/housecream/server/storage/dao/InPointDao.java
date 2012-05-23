package net.awired.housecream.server.storage.dao;

import net.awired.ajsl.persistence.dao.GenericDAOImpl;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import org.springframework.stereotype.Repository;

@Repository
public class InPointDao extends GenericDAOImpl<InPoint, Long> {

    public InPointDao() {
        super(InPoint.class, Long.class);
    }

}

package net.awired.housecream.server.storage.dao;

import java.util.List;
import javax.persistence.TypedQuery;
import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import org.springframework.stereotype.Repository;

@Repository
public class InPointDao extends GenericDaoImpl<InPoint, Long> {

    public InPointDao() {
        super(InPoint.class, Long.class);
    }

    public List<InPoint> findByZone(long zoneId) {
        TypedQuery<InPoint> query = entityManager.createNamedQuery(InPoint.QUERY_BY_ZONE, InPoint.class);
        query.setParameter(InPoint.QUERY_PARAM_ZONE_ID, zoneId);
        return findList(query);
    }

}

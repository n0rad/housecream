package net.awired.housecream.server.storage.dao;

import java.util.List;
import javax.persistence.TypedQuery;
import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import org.springframework.stereotype.Repository;

@Repository
public class OutPointDao extends GenericDaoImpl<OutPoint, Long> {

    public OutPointDao() {
        super(OutPoint.class, Long.class);
    }

    public List<OutPoint> findByZone(long zoneId) {
        TypedQuery<OutPoint> query = entityManager.createNamedQuery(OutPoint.QUERY_BY_ZONE, OutPoint.class);
        query.setParameter(OutPoint.QUERY_PARAM_ZONE_ID, zoneId);
        return findList(query);
    }
}

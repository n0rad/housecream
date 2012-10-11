package net.awired.housecream.server.storage.dao;

import java.util.List;
import javax.persistence.TypedQuery;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.ajsl.persistence.dao.impl.GenericDaoImpl;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.engine.InPointDaoInterface;
import org.springframework.stereotype.Repository;

@Repository
public class InPointDao extends GenericDaoImpl<InPoint, Long> implements InPointDaoInterface {

    public InPointDao() {
        super(InPoint.class, Long.class);
    }

    @Override
    public InPoint findFromUrl(String url) throws NotFoundException {
        TypedQuery<InPoint> query = entityManager.createNamedQuery(InPoint.QUERY_BY_URL, InPoint.class);
        query.setParameter(InPoint.QUERY_PARAM_URL, url);
        return findSingleResult(query);
    }

    public List<InPoint> findByZone(long zoneId) {
        TypedQuery<InPoint> query = entityManager.createNamedQuery(InPoint.QUERY_BY_ZONE, InPoint.class);
        query.setParameter(InPoint.QUERY_PARAM_ZONE_ID, zoneId);
        return findList(query);
    }

}

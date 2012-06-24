package net.awired.housecream.server.storage.dao;

import javax.persistence.TypedQuery;
import net.awired.ajsl.persistence.EntityNotFoundException;
import net.awired.ajsl.persistence.dao.GenericDAOImpl;
import net.awired.housecream.server.common.domain.inpoint.InPoint;
import org.springframework.stereotype.Repository;

@Repository
public class InPointDao extends GenericDAOImpl<InPoint, Long> {

    public InPointDao() {
        super(InPoint.class, Long.class);
    }

    public InPoint findFromUrl(String url) throws EntityNotFoundException {
        TypedQuery<InPoint> query = entityManager.createNamedQuery(InPoint.QUERY_BY_URL, InPoint.class);
        query.setParameter(InPoint.QUERY_PARAM_URL, url);
        InPoint singleResult = query.getSingleResult();
        return singleResult;
    }
}

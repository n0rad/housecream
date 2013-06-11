/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
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

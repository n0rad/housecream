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

import info.archinnov.achilles.entity.manager.ThriftEntityManager;
import java.util.List;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InPointDao {

    @Autowired
    private ThriftEntityManager em;

    public List<InPoint> findByZone(long zoneId) {
        //        TypedQuery<InPoint> query = entityManager.createNamedQuery(InPoint.QUERY_BY_ZONE, InPoint.class);
        //        query.setParameter(InPoint.QUERY_PARAM_ZONE_ID, zoneId);
        //        return findList(query);
        return null;
    }

    public InPoint find(long pointId) throws NotFoundException {
        return null;
    }

    public List<InPoint> findFiltered(Object object, Object object2, Object object3, Object object4, Object object5) {
        // TODO Auto-generated method stub
        return null;
    }

    public void delete(Long id) {
        // TODO Auto-generated method stub        
    }

    public List<InPoint> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    public InPoint save(InPoint inPoint) {
        // TODO Auto-generated method stub
        return null;
    }

    public Long findFilteredCount(String search, List<String> searchProperties) {
        return null;
    }

}

/**
 *
 *     Copyright (C) Housecream.org
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
package org.housecream.server.storage.dao;

import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.zone.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.Session;

@Repository
public class ZoneDao {

    private Session session;

    @Autowired
    public ZoneDao(Session session) {
        this.session = session;

    }

    public Zone find(UUID zoneId) {
        return null;
    }

    public List<Zone> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    public void delete(UUID zoneId) {
    }

    public void save(Zone zone) {
    }

    public void deleteAll() {
    }

    public List<OutPoint> findOutPoints(UUID zoneId) {
        return null;
    }

    public List<InPoint> findInPoints(UUID zoneId) {
        //        TypedQuery<InPoint> query = entityManager.createNamedQuery(InPoint.QUERY_BY_ZONE, InPoint.class);
        //        query.setParameter(InPoint.QUERY_PARAM_ZONE_ID, zoneId);
        //        return findList(query);
        return null;
    }

}

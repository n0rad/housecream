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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import org.springframework.stereotype.Repository;

@Repository
public class OutPointDao {

    public List<OutPoint> findByZone(UUID zoneId) {
        //        TypedQuery<OutPoint> query = entityManager.createNamedQuery(OutPoint.QUERY_BY_ZONE, OutPoint.class);
        //        query.setParameter(OutPoint.QUERY_PARAM_ZONE_ID, zoneId);
        //        return findList(query);
        return new ArrayList<>();
    }

    public OutPoint find(UUID outPointId) throws NotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<OutPoint> findAll() {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    public void delete(UUID id) {
        // TODO Auto-generated method stub
    }

    public void save(OutPoint outPoint) {
        // TODO Auto-generated method stub

    }

    public List<OutPoint> findFiltered(Integer length, UUID start) {
        // TODO Auto-generated method stub
        return null;
    }
}

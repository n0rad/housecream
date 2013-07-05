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

import info.archinnov.achilles.entity.manager.CQLEntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class InPointDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CQLEntityManager em;

    public List<InPoint> findByZone(UUID zoneId) {
        //        TypedQuery<InPoint> query = entityManager.createNamedQuery(InPoint.QUERY_BY_ZONE, InPoint.class);
        //        query.setParameter(InPoint.QUERY_PARAM_ZONE_ID, zoneId);
        //        return findList(query);
        return null;
    }

    public InPoint find(UUID pointId) throws NotFoundException {
        InPoint find = em.find(InPoint.class, pointId);
        if (find == null) {
            throw new NotFoundException("Cannot found InPoint with id : " + pointId);
        }
        return find;
    }

    public void delete(UUID id) {
        try {
            em.remove(find(id));
        } catch (NotFoundException e) {
            log.warn("cannot remove not found inpoint with id : " + id);
        }
    }

    public List<InPoint> findAll() {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    public InPoint save(InPoint inPoint) {
        em.persist(inPoint);
        return inPoint;
    }

    public List<InPoint> findFiltered(Integer length, UUID start) {
        // TODO Auto-generated method stub
        return null;
    }

}

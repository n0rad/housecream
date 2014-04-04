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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.zone.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import fr.norad.core.lang.exception.NotFoundException;

@Repository
public class ZoneDao {

    private Session session;
    private final PreparedStatement allStatement;
    private final PreparedStatement insertStatement;
    private final PreparedStatement deleteStatement;
    private final PreparedStatement selectStatement;

    @Autowired
    public ZoneDao(Session session) {
        this.session = session;
        allStatement = session.prepare("SELECT * from zones");
        insertStatement = session.prepare("BEGIN BATCH INSERT INTO zones(id, name, type) VALUES (?,?,?) APPLY BATCH");
        selectStatement = session.prepare("SELECT * FROM zones WHERE id = ?");
        deleteStatement = session.prepare("DELETE FROM zones WHERE id = ?");
    }

    public Zone find(UUID zoneId) throws NotFoundException {
        ResultSet execute = session.execute(selectStatement.bind(zoneId));
        if (execute.isExhausted()) {
            throw new NotFoundException("Cannot found zone with id : " + zoneId);
        }

        return map(execute.one());
    }

    public List<Zone> findAll() {
        ResultSet res = session.execute(allStatement.bind());
        List<Zone> zones = new ArrayList<>();
        for (Row row : res) {
            zones.add(map(row));
        }
        return zones;
    }

    public void delete(UUID zoneId) {
        session.execute(deleteStatement.bind(zoneId));
    }

    public Zone save(Zone zone) {
        if (zone.getId() == null) {
            zone.setId(UUID.randomUUID());
        }
        session.execute(insertStatement.bind(zone.getId(), //
                zone.getName(), //
                zone.type() //
        ));
        return zone;
    }

    public void deleteAll() {
        session.execute("TRUNCATE zones");
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

    private Zone map(Row row) {
        Zone zone = Zone.createZoneByType(row.getString("type"));
        zone.setId(row.getUUID("id"));
        zone.setName(row.getString("name"));
        return zone;
    }

}

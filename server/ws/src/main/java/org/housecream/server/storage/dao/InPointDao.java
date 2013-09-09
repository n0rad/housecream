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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import fr.norad.core.lang.exception.NotFoundException;

@Repository
public class InPointDao {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Session session;

    private final PreparedStatement insertQuery;
    private final PreparedStatement selectQuery;
    private final PreparedStatement allQuery;
    private final PreparedStatement deleteQuery;

    @Autowired
    public InPointDao(Session session) {
        this.session = session;
        insertQuery = session.prepare("BEGIN BATCH INSERT INTO inpoints(id, name, uri) VALUES (?,?,?) APPLY BATCH");
        selectQuery = session.prepare("SELECT * FROM inpoints WHERE id = ?");
        allQuery = session.prepare("SELECT * FROM inpoints");
        deleteQuery = session.prepare("DELETE FROM inpoints WHERE id = ?");
    }

    public InPoint find(UUID pointId) throws NotFoundException {
        ResultSet execute = session.execute(selectQuery.bind(pointId));
        if (execute.isExhausted()) {
            throw new NotFoundException("Cannot found InPoint with id : " + pointId);
        }

        return map(execute.one());
    }

    public void delete(UUID id) {
        session.execute(deleteQuery.bind(id));
    }

    public void deleteAll() {
        session.execute("TRUNCATE inpoints");
    }

    public List<InPoint> findAll() {
        ResultSet res = session.execute(allQuery.bind());
        List<InPoint> inPoints = new ArrayList<>();
        for (Row row : res) {
            inPoints.add(map(row));
        }
        return inPoints;
    }

    public InPoint save(InPoint inPoint) {
        if (inPoint.getId() == null) {
            inPoint.setId(UUID.randomUUID());
        }
        session.execute(insertQuery.bind(inPoint.getId(), //
                inPoint.getName(), //
                inPoint.getUri().toString()));
        return inPoint;
    }

    public List<InPoint> findFiltered(Integer length, UUID start) {
        // TODO Auto-generated method stub
        return null;
    }

    //////////////////////////////////////

    private InPoint map(Row row) {
        InPoint inPoint = new InPoint();
        inPoint.setId(row.getUUID("id"));
        inPoint.setName(row.getString("name"));
        String uri = row.getString("uri");
        if (uri != null) {
            inPoint.setUri(URI.create(uri));
        }
        return inPoint;
    }

}

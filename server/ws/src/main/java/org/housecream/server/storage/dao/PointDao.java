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
import org.housecream.server.api.domain.point.Point;
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
public class PointDao {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Session session;

    private final PreparedStatement insertQuery;
    private final PreparedStatement selectQuery;
    private final PreparedStatement allQuery;
    private final PreparedStatement deleteQuery;

    @Autowired
    public PointDao(Session session) {
        this.session = session;
        insertQuery = session.prepare("BEGIN BATCH INSERT INTO points(id, name, uri) VALUES (?,?,?) APPLY BATCH");
        selectQuery = session.prepare("SELECT * FROM points WHERE id = ?");
        allQuery = session.prepare("SELECT * FROM points");
        deleteQuery = session.prepare("DELETE FROM points WHERE id = ?");
    }

    public Point find(UUID pointId) throws NotFoundException {
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

    public List<Point> findAll() {
        ResultSet res = session.execute(allQuery.bind());
        List<Point> inPoints = new ArrayList<>();
        for (Row row : res) {
            inPoints.add(map(row));
        }
        return inPoints;
    }

    public Point save(Point point) {
        if (point.getId() == null) {
            point.setId(UUID.randomUUID());
        }
        session.execute(insertQuery.bind(point.getId(), //
                point.getName(), //
                point.getUri().toString()));
        return point;
    }

    //////////////////////////////////////

    private Point map(Row row) {
        Point point = new Point();
        point.setId(row.getUUID("id"));
        point.setName(row.getString("name"));
        String uri = row.getString("uri");
        if (uri != null) {
            point.setUri(URI.create(uri));
        }
        return point;
    }

}

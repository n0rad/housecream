package org.housecream.server.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;

@Repository
public class ValueDao {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Session session;

    private final PreparedStatement insertQuery;
    private final PreparedStatement selectQuery;
    private final PreparedStatement allQuery;
    private final PreparedStatement deleteQuery;

    @Autowired
    public ValueDao(Session session) {
        this.session = session;
        insertQuery = session.prepare("BEGIN BATCH INSERT INTO points(id, name, uri) VALUES (?,?,?) APPLY BATCH");
        selectQuery = session.prepare("SELECT * FROM points WHERE id = ?");
        allQuery = session.prepare("SELECT * FROM points");
        deleteQuery = session.prepare("DELETE FROM points WHERE id = ?");
    }

}

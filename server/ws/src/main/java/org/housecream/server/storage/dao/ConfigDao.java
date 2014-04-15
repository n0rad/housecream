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

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

@Repository
public class ConfigDao {

    private final Session session;
    private final PreparedStatement updateField;
    private final PreparedStatement update;
    private final PreparedStatement select;

    @Autowired
    public ConfigDao(Session session) {
        this.session = session;
        updateField = session.prepare("UPDATE config SET properties[?] = ? WHERE id = 'Housecream'");
        update = session.prepare("UPDATE config SET properties = ? WHERE id = 'Housecream'");
        select = session.prepare("SELECT * FROM config WHERE id = 'Housecream'");
    }

    public Map<String, String> loadConfig() {
        ResultSet execute = session.execute(select.bind());
        Map<String, String> properties = execute.one().getMap("properties", String.class, String.class);
        return properties;
    }

    public void saveConfig(Map<String, String> properties) {
        session.execute(update.bind(properties));
    }

    public void saveProperty(String name, String value) {
        session.execute(updateField.bind(name, value));
    }
}

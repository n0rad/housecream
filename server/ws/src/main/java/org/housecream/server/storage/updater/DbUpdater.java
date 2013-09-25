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
package org.housecream.server.storage.updater;

import static com.datastax.driver.core.querybuilder.QueryBuilder.select;
import java.util.HashSet;
import java.util.Set;
import org.housecream.server.Housecream;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import fr.norad.core.updater.ApplicationUpdater;
import fr.norad.core.updater.Version;

public class DbUpdater extends ApplicationUpdater {

    private final Session session;
    private final PreparedStatement updateStatement;
    private final PreparedStatement selectStatement;

    public DbUpdater(Session session) {
        super(Housecream.HOUSECREAM_NAME, new V0(session));
        this.session = session;
        try {
            session.execute("CREATE TABLE version (version text PRIMARY KEY, names set<text>)");
        } catch (AlreadyExistsException e) {
            //nothing to do
        }
        updateStatement = session.prepare("INSERT INTO version(version, names) VALUES (?, ?)");
        selectStatement = session.prepare("SELECT names FROM version WHERE version = ?");
    }

    @Override
    protected Version getCurrentVersion() {
        try {
            Row version = session.execute(select("version").from("version")).one();
            if (version == null) {
                return null;
            }
            return Version.parse(version.getString(0));
        } catch (InvalidQueryException e) {
            return null;
        }
    }

    @Override
    protected Set<String> getUpdatedNames(Version version) {
        ResultSet res = session.execute(selectStatement.bind(version.toString()));
        if (res.isExhausted()) {
            return null;
        }
        Set<String> set = res.one().getSet("names", String.class);
        return set;
    }

    @Override
    protected void addUpdatedName(Version version, String name) {
        HashSet<Object> hashSet = new HashSet<>();
        hashSet.add(name);
        Set<String> updatedNames = getUpdatedNames(version);
        if (updatedNames != null) {
            hashSet.addAll(updatedNames);
        }
        session.execute(updateStatement.bind(version.toString(), hashSet));
    }
}

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

import static fr.norad.core.updater.Version.V;
import com.datastax.driver.core.Session;
import fr.norad.core.updater.ApplicationVersion;
import fr.norad.core.updater.Update;

public class V0 extends ApplicationVersion {

    public V0(final Session session) {
        super(V(0), new Update("create outpoints CF") {
            @Override
            public void runUpdate() {
                session.execute("CREATE TABLE inPoints(id UUID," //
                        + "name text," //
                        + "uri text," //
                        + " PRIMARY KEY(id));");
                session.execute("CREATE TABLE outPoints(id UUID," //
                        + "name text," //
                        + "uri text," //
                        + " PRIMARY KEY(id));");
            }
        }, new Update("create inpoints cf") {
            @Override
            public void runUpdate() {
                session.execute("CREATE TABLE rules(id UUID PRIMARY KEY, " //
                        + "name text," //
                        + "conditions list<text>," //
                        + "consequences list<text>," //
                        + "salience int )");
                session.execute("CREATE TABLE zones(id UUID PRIMARY KEY," //
                        + "name text," //
                        + "type text)");
                session.execute("CREATE TABLE pointZone(id UUID PRIMARY KEY," //
                        + "name text," //
                        + "parentId UUID)");
            }
        });
    }
}

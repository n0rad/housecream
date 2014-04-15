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

import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.google.common.collect.Sets.newHashSet;
import static fr.norad.updater.Version.V;
import org.housecream.server.api.Security.Scopes;
import com.datastax.driver.core.Session;
import fr.norad.updater.ApplicationVersion;
import fr.norad.updater.Update;

public class V0 extends ApplicationVersion {

    public V0(final Session session) {
        super(V(0), new Update("Create config") {
                    @Override
                    public void runUpdate() {
                        session.execute("CREATE TABLE config(" +
                                "id text PRIMARY KEY," +
                                "properties map<text, text>)");
                    }
                }, new Update("create business") {
                    @Override
                    public void runUpdate() {
                        session.execute("CREATE TABLE points(id UUID," //
                                + "name text," //
                                + "uri text," //
                                + " PRIMARY KEY(id));");
                        session.execute("CREATE TABLE values(" +
                                "year int," +
                                "month int," +
                                "date timestamp," +
                                "value blob," +
                                "PRIMARY KEY ((year, month), date))");

                        session.execute("CREATE TABLE rules(" +
                                "id UUID PRIMARY KEY," +
                                "name text," +
                                "conditions list<text>," +
                                "consequences list<text>," +
                                "salience int)");
                        session.execute("CREATE TABLE zones(id UUID PRIMARY KEY, name text, type text)");
                        session.execute("CREATE TABLE pointZone(id UUID PRIMARY KEY, name text, parentId UUID)");
                    }
                }, new Update("Create security part") {
                    @Override
                    public void runUpdate() throws Exception {
                        session.execute("CREATE TABLE users(" +
                                "username text PRIMARY KEY," +
                                "hashedPassword text," +
                                "salt text," +
                                "groups set<text>," +
                                "failedLogin set<timestamp>" +
                                ")");
                        session.execute("CREATE TABLE tokens(" +
                                "tokenType text," +
                                "issuedAt bigint, " +
                                "grantType text," +
                                "lifetime int," +
                                "username text," +
                                "refreshToken text," +
                                "clientId text," +
                                "accessToken text PRIMARY KEY," +
                                "scopes set<text>" +
                                ")");
                        session.execute("CREATE TABLE clients(" +
                                "id text PRIMARY KEY," +
                                "secret text," +
                                "description text," +
                                "tokenLifetime int," +
                                "refreshTokenLifetime int," +
                                "allowedGrantTypes set<text>," +
                                "allowedScopes set<text>)");
                        session.execute("CREATE TABLE refresh_tokens(" +
                                "refreshToken text PRIMARY KEY," +
                                "sourceAccessToken text," +
                                "issuedAt bigint," +
                                "lifetime int," +
                                "grantType text, " +
                                "username text," +
                                "clientId text," +
                                "scopes set<text>" +
                                ")");
                        session.execute("CREATE TABLE authorizations(code text PRIMARY KEY, username text)");
                        session.execute("CREATE TABLE groups(name text PRIMARY KEY, allowedScopes set<text>, usernames set<text>)");

                        session.execute(insertInto("groups")
                                .value("name", "administrators")
                                .value("allowedScopes", Scopes.toStrings())
                                .value("usernames", newHashSet("admin")));
                    }
                }
        );
    }
}

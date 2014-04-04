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
import static org.housecream.server.api.resource.Security.Scopes.toStrings;
import static org.housecream.server.application.config.EncodingConfig.objectMapper;
import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.application.security.RandomStringGenerator;
import org.housecream.server.storage.dao.HcPropertiesDao;
import com.datastax.driver.core.Session;
import fr.norad.jaxrs.oauth2.api.Group;
import fr.norad.jaxrs.oauth2.core.service.PasswordHasher;
import fr.norad.updater.ApplicationVersion;
import fr.norad.updater.Update;

public class V0 extends ApplicationVersion {

    private static final HcProperties props = new HcProperties().setSecurityGlobalSaltSecret(new RandomStringGenerator().base64(42));

    public V0(final Session session) {
        super(V(0), new Update("create config") {
                    @Override
                    public void runUpdate() {
                        session.execute("CREATE TABLE config(" +
                                "id text PRIMARY KEY," +
                                "properties map<text, text>)");
                        new HcPropertiesDao(session).saveConfig(props); // todo using dao will cause pb
                    }
                }, new Update("create outpoints CF") {
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
                                .value("allowedScopes", toStrings())
                                .value("usernames", newHashSet("admin")));

                        String adminSalt = new RandomStringGenerator().base64(128);
                        PasswordHasher passwordHasher = new PasswordHasher(props.getSecurityGlobalSaltSecret());
                        session.execute(insertInto("users")
                                .value("username", "admin")
                                .value("hashedPassword", passwordHasher.hash("admin", adminSalt))
                                .value("salt", adminSalt)
                                .value("groups", newHashSet(objectMapper().writeValueAsString(new Group("administrators", toStrings())))));
                    }
                }
        );
    }
}

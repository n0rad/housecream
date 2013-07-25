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
package org.housecream.server.it.api;

import org.housecream.server.api.domain.user.User;
import org.housecream.server.api.resource.UsersResource;
import org.housecream.server.it.HcWsItServer;
import org.housecream.server.it.HcWsItSession;

public class UserApi {

    private HcWsItSession session;

    public UserApi(HcWsItServer server) {
        //        this.session = new HcWsItSession(server);
    }

    public UserApi(HcWsItSession session) {
        this.session = session;
    }

    public User create(String username, String password) {
        return session.getServer().getResource(UsersResource.class, session).createUser(new User(username, password));
    }
}

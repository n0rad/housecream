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
package org.housecream.server.it.core;

import fr.norad.jaxrs.client.server.rest.RestClient;
import fr.norad.jaxrs.oauth2.api.Client;

public class ItClient extends RestClient {

    public ItClient(String id, String secret) {
        super(id, secret);
    }

    public ItClient(Client client) {
        super(client.getId(), client.getSecret());
    }
}

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
package org.housecream.server.api.resource;


import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.housecream.server.api.Security;
import org.housecream.server.api.Security.Scopes;
import org.housecream.server.api.domain.config.Config;

@Security(Scopes.CONFIG_READ)
@Path("/configs")
public interface ConfigsResource {

    @GET
    Config getConfigs();

    @Path("{configName}")
    ConfigResource config(@PathParam("configName") String name);

    interface ConfigResource {
        @PUT
        @Security(Scopes.CONFIG_WRITE)
        void setConfig(@PathParam("configName") String name, String value);
    }
}

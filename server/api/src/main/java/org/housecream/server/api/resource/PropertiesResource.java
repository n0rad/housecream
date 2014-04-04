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
import org.housecream.server.api.domain.HcProperties;
import org.housecream.server.api.resource.Security.Scopes;

@Security(Scopes.PROPERTIES_READ)
@Path("/properties")
public interface PropertiesResource {

    @GET
    HcProperties getProperties();

    @Path("{propertyName}")
    PropertyResource property(@PathParam("propertyName") String propertyName);

    interface PropertyResource {
        @PUT
        @Security(Scopes.PROPERTIES_WRITE)
        void setProperty(@PathParam("propertyName") String propertyName, String propertyValue);
    }
}

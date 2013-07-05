/**
 *
 *     Housecream.org project
 *     Copyright (C) Awired.net
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
package net.awired.housecream.server.api.resource;

import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import net.awired.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.resource.generic.PointResource;

@Path("/inpoints/{id:\\d+}")
public interface InPointResource extends PointResource {

    @PUT
    InPoint updateInPoint(@PathParam("id") UUID inPointId, @Valid InPoint inPoint) throws PluginNotFoundException;

    @GET
    InPoint getInPoint(@PathParam("id") UUID inPointId) throws NotFoundException;

    @DELETE
    void deleteInPoint(@PathParam("id") UUID inPointId);
    //    @GET
    //    @Path("/value")
    //    Float getInPointValue(@PathParam("id") long inPointId);

}

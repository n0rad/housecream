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

import static org.housecream.server.api.resource.Security.Scopes.INPOINT_READ;
import static org.housecream.server.api.resource.Security.Scopes.INPOINT_WRITE;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.inpoint.InPointType;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.resource.generic.PointResource;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.core.lang.exception.NotFoundException;

@Path("/inpoints")
@Security(INPOINT_READ)
public interface InPointsResource {

    @GET
    @Path("/validator")
    public ClientValidatorInfo getInPointValidator();

    @POST
    @Security(INPOINT_WRITE)
    InPoint createInPoint(@Valid InPoint inPoint) throws PluginNotFoundException;

    @GET
    List<InPoint> getInPoints();

    @DELETE
    @Security(INPOINT_WRITE)
    void deleteAllInPoints();

    @GET
    @Path("/types")
    List<InPointType> getInPointTypes();

    ///////////////////////

    @Path("/{id:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    InPointResource inPoint(@PathParam("id") UUID inPointId);

    interface InPointResource extends PointResource {

        @PUT
        @Security(INPOINT_WRITE)
        InPoint updateInPoint(@PathParam("id") UUID inPointId, @Valid InPoint inPoint) throws PluginNotFoundException;

        @GET
        InPoint getInPoint(@PathParam("id") UUID inPointId) throws NotFoundException;

        @DELETE
        @Security(INPOINT_WRITE)
        void deleteInPoint(@PathParam("id") UUID inPointId);
        //    @GET
        //    @Path("/value")
        //    Float getInPointValue(@PathParam("id") long inPointId);

    }

}

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

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.outPoint.OutPointType;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.resource.Security.Scopes;
import org.housecream.server.api.resource.generic.PointResource;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.core.lang.exception.NotFoundException;

@Path("/outpoints")
@Security(Scopes.OUTPOINT_READ)
public interface OutPointsResource {

    @GET
    List<OutPoint> getInPoints();

    @DELETE
    @Security(Scopes.OUTPOINT_WRITE)
    void deleteAllOutPoints();

    @GET
    @Path("/validator")
    ClientValidatorInfo getOutPointValidator();

    @PUT
    @Security(Scopes.OUTPOINT_WRITE)
    OutPoint createOutPoint(@Valid OutPoint outPoint) throws PluginNotFoundException;

    @GET
    @Path("/types")
    List<OutPointType> getOutPointTypes();

    @Path("/{id:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    OutPointResource outPoint(@PathParam("id") UUID outPointId);

    public interface OutPointResource extends PointResource {

        @GET
        OutPoint getOutPoint(@PathParam("id") UUID outPointId) throws NotFoundException;

        @DELETE
        @Security(Scopes.OUTPOINT_WRITE)
        void deleteOutPoint(@PathParam("id") UUID outPointId);

        @PUT
        @Path("/value")
        @Security(Scopes.OUTPOINT_WRITE)
        void setValue(@PathParam("id") UUID outPointId, Float value) throws Exception;

    }

}

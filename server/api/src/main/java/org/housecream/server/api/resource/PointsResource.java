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

import static org.housecream.server.api.Security.Scopes.POINT_READ;
import static org.housecream.server.api.Security.Scopes.POINT_WRITE;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.housecream.server.api.Security;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.point.PointType;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.exception.PointNotFoundException;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;

@Path("/points")
@Security(POINT_READ)
public interface PointsResource {

    @GET
    @Path("/validator")
    public ClientValidatorInfo getPointValidator();

    @POST
    @Security(POINT_WRITE)
    Point createInPoint(@NotNull @Valid Point point) throws PluginNotFoundException;

    @GET
    List<Point> getPoints();

    @DELETE
    @Security(POINT_WRITE)
    void deleteAllPoints();

    @GET
    @Path("/types")
    List<PointType> getPointTypes();

    ///////////////////////

    @Path("/{id:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}}")
    PointResource point(@PathParam("id") UUID pointId) throws PointNotFoundException;

    interface PointResource {

        @PUT
        @Security(POINT_WRITE)
        Point updatePoint(@PathParam("id") UUID pointId, @NotNull @Valid Point point) throws PointNotFoundException, PluginNotFoundException;

        @GET
        Point getPoint(@PathParam("id") UUID pointId) throws PointNotFoundException;

        @DELETE
        @Security(POINT_WRITE)
        void deletePoint(@PathParam("id") UUID pointId);

        @GET
        @Path("/value")
        Object getValue(@PathParam("id") UUID pointId) throws PointNotFoundException;

        @PUT
        @Path("/value")
        @Security(POINT_WRITE)
        void setValue(UUID pointId, Object value) throws PointNotFoundException;

    }

}

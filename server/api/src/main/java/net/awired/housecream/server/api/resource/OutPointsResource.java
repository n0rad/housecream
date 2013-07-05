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

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import net.awired.client.bean.validation.js.domain.ClientValidatorInfo;
import net.awired.housecream.server.api.domain.Order;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;

@Path("/outpoints")
public interface OutPointsResource {

    @GET
    List<OutPoint> getInPoints(@QueryParam("length") Integer length, //
            @QueryParam("start") UUID start, //
            @QueryParam("search") String search, //
            @QueryParam("searchProperty") List<String> searchProperties, //
            @QueryParam("order") List<Order> orders);

    @DELETE
    void deleteAllOutPoints();

    @GET
    @Path("/validator")
    ClientValidatorInfo getOutPointValidator();

    @PUT
    OutPoint createOutPoint(@Valid OutPoint outPoint) throws PluginNotFoundException;

    @GET
    @Path("/types")
    List<OutPointType> getOutPointTypes();
}

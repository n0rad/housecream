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
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.housecream.server.api.domain.Order;
import org.housecream.server.api.domain.zone.Zone;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;

@Path("/zones")
public interface ZonesResource {

    @GET
    List<Zone> getZones(@QueryParam("length") Integer length, //
            @QueryParam("start") UUID start, //
            @QueryParam("search") String search, //
            @QueryParam("searchProperty") List<String> searchProperties, //
            @QueryParam("order") List<Order> orders);

    @GET
    @Path("/validator")
    Map<String, ClientValidatorInfo> getZoneValidator();

    @POST
    Zone createZone(@Valid Zone zone) throws RuntimeException;

    @DELETE
    void deleteAllZones();
}
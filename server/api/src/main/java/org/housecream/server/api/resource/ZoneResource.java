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
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.zone.Zone;
import fr.norad.core.lang.exception.NotFoundException;

@Path("/zones/{id}")
public interface ZoneResource {

    @PUT
    Zone updateZone(@PathParam("id") UUID zoneId, @Valid Zone zone) throws NotFoundException;

    @GET
    Zone getZone(@PathParam("id") UUID zoneId) throws NotFoundException;

    @DELETE
    void deleteZone(@PathParam("id") UUID zoneId);

    @POST
    @Path("/image")
    @Consumes("multipart/form-data")
    void uploadImage(@PathParam("id") UUID zoneId, MultipartBody body) throws NotFoundException;

    @GET
    @Path("/image")
    Response getImage(@PathParam("id") UUID zoneId) throws NotFoundException;

    @GET
    @Path("/inpoints")
    List<InPoint> getInPoints(@PathParam("id") UUID zoneId);

    @GET
    @Path("/outpoints")
    List<OutPoint> getOutPoints(@PathParam("id") UUID zoneId);

}

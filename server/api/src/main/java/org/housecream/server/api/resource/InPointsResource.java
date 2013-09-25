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
import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.inpoint.InPointType;
import org.housecream.server.api.security.Scopes;
import fr.norad.client.bean.validation.js.domain.ClientValidatorInfo;
import fr.norad.jaxrs.oauth2.Secured;

@Path("/inpoints")
public interface InPointsResource {

    @GET
    @Path("/validator")
    public ClientValidatorInfo getInPointValidator();

    @POST
    InPoint createInPoint(@Valid InPoint inPoint) throws PluginNotFoundException;

    @GET
    @Secured(Scopes.Events.class)
    List<InPoint> getInPoints();

    @DELETE
    void deleteAllInPoints();

    @GET
    @Path("/types")
    List<InPointType> getInPointTypes();

}

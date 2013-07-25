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
package org.housecream.server.it.api;

import static org.housecream.server.it.builder.InPointBuilder.in;
import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.inpoint.InPoint;
import org.housecream.server.api.domain.inpoint.InPointType;
import org.housecream.server.api.domain.zone.Zone;
import org.housecream.server.api.resource.InPointResource;
import org.housecream.server.api.resource.InPointsResource;
import org.housecream.server.api.resource.PluginNotFoundException;
import org.housecream.server.it.HcWsItSession;
import fr.norad.core.lang.exception.NotFoundException;

public class InpointApi {

    private final HcWsItSession session;

    public InpointApi(HcWsItSession session) {
        this.session = session;
    }

    public InPoint create(String name, Zone zone, InPointType type, String uri) throws PluginNotFoundException {
        InPoint inpoint = session.getServer().getResource(InPointsResource.class, session)
                .createInPoint(in().name(name).uri(uri).type(type).zoneId(zone.getId()).build());
        return inpoint;
    }

    public List<InPoint> list() {
        return session.getServer().getResource(InPointsResource.class, session)
                .getInPoints(null, null, null, null, null);
    }

    public void deleteAll() {
        session.getServer().getResource(InPointsResource.class, session).deleteAllInPoints();
    }

    public InPoint getPoint(UUID pointId) throws NotFoundException {
        return session.getServer().getResource(InPointResource.class, session).getInPoint(pointId);
    }

    public InPointResource internalInpointResource() {
        return session.getServer().getResource(InPointResource.class, session);
    }
}

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
package net.awired.housecream.server.it.api;

import static net.awired.housecream.server.it.builder.InPointBuilder.in;
import net.awired.ajsl.core.lang.exception.NotFoundException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;
import net.awired.housecream.server.api.domain.inpoint.InPoints;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.resource.InPointResource;
import net.awired.housecream.server.api.resource.InPointsResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.it.HcWsItSession;

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

    public InPoints list() {
        return session.getServer().getResource(InPointsResource.class, session)
                .getInPoints(null, null, null, null, null);
    }

    public void deleteAll() {
        session.getServer().getResource(InPointsResource.class, session).deleteAllInPoints();
    }

    public InPoint getPoint(long pointId) throws NotFoundException {
        return session.getServer().getResource(InPointResource.class, session).getInPoint(pointId);
    }

    public InPointResource internalInpointResource() {
        return session.getServer().getResource(InPointResource.class, session);
    }
}

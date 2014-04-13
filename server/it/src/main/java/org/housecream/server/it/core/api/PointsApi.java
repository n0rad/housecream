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
package org.housecream.server.it.core.api;

import static org.housecream.server.it.builder.PointBuilder.in;
import java.util.List;
import java.util.UUID;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.point.PointType;
import org.housecream.server.api.exception.PluginNotFoundException;
import org.housecream.server.api.exception.PointNotFoundException;
import org.housecream.server.api.resource.PointsResource;
import org.housecream.server.api.resource.PointsResource.PointResource;
import org.housecream.server.it.core.ItSession;

public class PointsApi {

    private final ItSession session;

    public PointsApi(ItSession session) {
        this.session = session;
    }

    public Point create(String name, PointType type, String uri) throws PluginNotFoundException {
        Point inpoint = session.getServer().getResource(PointsResource.class, session)
                .createInPoint(in().name(name).uri(uri).type(type).build());
        return inpoint;
    }

    public List<Point> list() {
        return session.getServer().getResource(PointsResource.class, session).getPoints();
    }

    public void deleteAll() {
        session.getServer().getResource(PointsResource.class, session).deleteAllPoints();
    }

    public Point getPoint(UUID pointId) throws PointNotFoundException {
        return session.getServer().getResource(PointsResource.class, session).point(pointId).getPoint(pointId);
    }

    public PointResource internalInpointResource(UUID inPointId) throws PointNotFoundException {
        return session.getServer().getResource(PointsResource.class, session).point(inPointId);
    }


    public void setValue(Point point, float value) throws Exception {
        session.getServer().getResource(PointsResource.class, session).point(point.getId()).setValue(point.getId(), value);
    }
}

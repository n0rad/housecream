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

import static org.housecream.server.it.builder.OutPointBuilder.out;
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.outPoint.OutPointType;
import org.housecream.server.api.domain.zone.Zone;
import org.housecream.server.api.resource.OutPointResource;
import org.housecream.server.api.resource.OutPointsResource;
import org.housecream.server.api.resource.PluginNotFoundException;
import org.housecream.server.it.HcWsItSession;

public class OutpointApi {

    private final HcWsItSession session;

    public OutpointApi(HcWsItSession session) {
        this.session = session;
    }

    public OutPoint create(String name, Zone zone, OutPointType type, String uri) throws PluginNotFoundException {
        OutPoint outpoint = session.getServer().getResource(OutPointsResource.class, session)
                .createOutPoint(out().name(name).uri(uri).type(type).zoneId(zone.getId()).build());
        return outpoint;
    }

    public void deleteAll() {
        session.getServer().getResource(OutPointsResource.class, session).deleteAllOutPoints();
    }

    public void setValue(OutPoint outpoint, float value) throws Exception {
        session.getServer().getResource(OutPointResource.class, session).setValue(outpoint.getId(), value);
    }
}

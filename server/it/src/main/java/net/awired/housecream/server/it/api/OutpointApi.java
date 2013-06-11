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

import static net.awired.housecream.server.it.builder.OutPointBuilder.out;
import net.awired.housecream.server.api.domain.outPoint.OutPoint;
import net.awired.housecream.server.api.domain.outPoint.OutPointType;
import net.awired.housecream.server.api.domain.zone.Zone;
import net.awired.housecream.server.api.resource.OutPointResource;
import net.awired.housecream.server.api.resource.OutPointsResource;
import net.awired.housecream.server.api.resource.PluginNotFoundException;
import net.awired.housecream.server.it.HcWsItSession;

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

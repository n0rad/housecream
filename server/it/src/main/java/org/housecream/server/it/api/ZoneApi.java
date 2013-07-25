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

import org.housecream.server.api.domain.zone.Land;
import org.housecream.server.api.resource.ZonesResource;
import org.housecream.server.it.HcWsItSession;
import org.housecream.server.it.builder.zone.LandBuilder;

public class ZoneApi {

    private final HcWsItSession session;

    public ZoneApi(HcWsItSession session) {
        this.session = session;
    }

    public Land createLand(String name) {
        Land land = (Land) session.getServer().getResource(ZonesResource.class, session)
                .createZone(new LandBuilder().name(name).build());
        return land;
    }

    public void deleteAll() {
        session.getServer().getResource(ZonesResource.class, session).deleteAllZones();
    }
}

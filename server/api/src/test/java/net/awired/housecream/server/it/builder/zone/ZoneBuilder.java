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
package net.awired.housecream.server.it.builder.zone;

import java.util.UUID;
import net.awired.housecream.server.api.domain.zone.Zone;

public abstract class ZoneBuilder<T extends ZoneBuilder<T>> {

    private UUID id;
    private String name;
    private Long parentId;

    protected void build(Zone zone) {
        zone.setId(id);
        zone.setName(name);
        zone.setParentId(parentId);
    }

    //////////////////

    public T id(UUID id) {
        this.id = id;
        return (T) this;
    }

    public T name(String name) {
        this.name = name;
        return (T) this;
    }

    public T parentId(Long parentId) {
        this.parentId = parentId;
        return (T) this;
    }

}

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
package net.awired.housecream.server.it.builder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;

public class InPointBuilder {

    private UUID id;
    private String name;
    private InPointType type;
    private URI uri;
    private UUID zoneId;

    public static InPointBuilder in() {
        return new InPointBuilder();
    }

    public InPoint build() {
        InPoint inPoint = new InPoint();
        inPoint.setId(id);
        inPoint.setName(name);
        inPoint.setType(type);
        inPoint.setUri(uri);
        if (zoneId != null) {
            inPoint.setZoneId(zoneId);
        }
        return inPoint;
    }

    public InPointBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public InPointBuilder zoneId(UUID zoneId) {
        this.zoneId = zoneId;
        return this;
    }

    public InPointBuilder name(String name) {
        this.name = name;
        return this;
    }

    public InPointBuilder type(InPointType type) {
        this.type = type;
        return this;
    }

    public InPointBuilder uri(String stringUri) {
        try {
            this.uri = new URI(stringUri);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    public InPointBuilder uri(URI uri) {
        this.uri = uri;
        return this;
    }
}

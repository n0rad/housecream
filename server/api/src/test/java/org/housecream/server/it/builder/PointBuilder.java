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
package org.housecream.server.it.builder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.housecream.server.api.domain.point.Point;
import org.housecream.server.api.domain.point.PointType;

public class PointBuilder {

    private UUID id;
    private String name;
    private PointType type;
    private URI uri;

    public static PointBuilder in() {
        return new PointBuilder();
    }

    public Point build() {
        Point inPoint = new Point();
        inPoint.setId(id);
        inPoint.setName(name);
        inPoint.setType(type);
        inPoint.setUri(uri);
        return inPoint;
    }

    public PointBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public PointBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PointBuilder type(PointType type) {
        this.type = type;
        return this;
    }

    public PointBuilder uri(String stringUri) {
        try {
            this.uri = new URI(stringUri);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    public PointBuilder uri(URI uri) {
        this.uri = uri;
        return this;
    }
}

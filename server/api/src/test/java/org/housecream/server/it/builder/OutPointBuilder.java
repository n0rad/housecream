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
import org.housecream.server.api.domain.outPoint.OutPoint;
import org.housecream.server.api.domain.outPoint.OutPointType;

public class OutPointBuilder {

    private URI uri;
    private OutPointType type;
    private String name;
    private UUID zoneId;

    public static OutPointBuilder out() {
        return new OutPointBuilder();
    }

    public OutPoint build() {
        OutPoint outPoint = new OutPoint();
        outPoint.setName(name);
        outPoint.setType(type);
        outPoint.setUri(uri);
        outPoint.setZoneId(zoneId);
        return outPoint;
    }

    public OutPointBuilder uri(String stringUri) {
        try {
            this.uri = new URI(stringUri);
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
        return this;
    }

    public OutPointBuilder uri(URI uri) {
        this.uri = uri;
        return this;
    }

    public OutPointBuilder type(OutPointType type) {
        this.type = type;
        return this;
    }

    public OutPointBuilder name(String name) {
        this.name = name;
        return this;
    }

    public OutPointBuilder zoneId(UUID zoneId) {
        this.zoneId = zoneId;
        return this;
    }

}

package net.awired.housecream.server.it.builder;

import java.net.URI;
import java.net.URISyntaxException;
import net.awired.housecream.server.api.domain.inpoint.InPoint;
import net.awired.housecream.server.api.domain.inpoint.InPointType;

public class InPointBuilder {

    private Long id;
    private String name;
    private InPointType type;
    private URI uri;
    private Long zoneId;

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

    public InPointBuilder id(long id) {
        this.id = id;
        return this;
    }

    public InPointBuilder zoneId(long zoneId) {
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
